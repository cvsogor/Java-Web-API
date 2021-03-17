package com.aditor.dashboard.spring.processing;

import com.aditor.dashboard.spring.Dao;
import com.aditor.dashboard.spring.model.*;
import com.aditor.dashboard.spring.util.GoogleStorageProvider;
import com.aditor.dashboard.spring.util.ZipUtil;
import com.aditor.dashboard.spring.validation.AppsflyerReportProcessor;
import com.aditor.dashboard.spring.validation.MandatoryColumnsMissingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by dgordiichuk on 03.02.2016.
 */
@Repository
public class AppsflyerProcessing {

    private final static Logger logger = LoggerFactory.getLogger(AppsflyerProcessing.class);
    private final static double MAX_FAILED_RECORDS_RATIO = 0.8;
    private final static String bucketName = "non-rtb-backup";

    @Autowired
    private Dao dao;
    @Autowired
    private AppsflyerReportProcessor reportProcessor;

    public boolean importFromAppsflyer(LocalDate fromDate, LocalDate toDate, StringBuffer message, List<ReportType> reportTypeList, List<AppsflyerImportConfig> offers, List<AppsflyerImportConfig> offersFull) {
        long startTime = System.currentTimeMillis()/1000L;
        try {
            logger.info("Start upload data from appsflyer");
            logger.info("Start upload data to " + AppsflyerTableType.GENERAL);
            UploadCount uploadCount = importForOneConfiguration(fromDate, toDate, reportTypeList, offers, AppsflyerTableType.GENERAL);
            logger.info("Start upload data to " + AppsflyerTableType.FULL);
            uploadCount.addUploadCount(importForOneConfiguration(fromDate, toDate, reportTypeList, offersFull, AppsflyerTableType.FULL));

            int errors = (offers.size() * reportTypeList.size() + offersFull.size() * reportTypeList.size() - uploadCount.getCountFile());
            long duration = System.currentTimeMillis()/1000L - startTime;
            message.append("Files upload finished successfully! " +
                    getUploadCountMessage(uploadCount, duration, errors));
            logger.info(message.toString());
            return true;
        } catch (Exception e) {
            logger.error("Unknown error during importFromAppsflyer", e);
            return false;
        }
    }

    private UploadCount importForOneConfiguration(LocalDate fromDate, LocalDate toDate, List<ReportType> reportTypeList, List<AppsflyerImportConfig> offers, AppsflyerTableType tableType) {
        long startTime = System.currentTimeMillis()/1000L;
        UploadCount uploadCount = new UploadCount();
        for (AppsflyerImportConfig importEntry: offers) {
            for (ReportType reportType : reportTypeList) {
                UploadCount count = importSingleFileFromAppsflyer(importEntry, reportType, fromDate, toDate, tableType);
                uploadCount.addUploadCount(count);
            }
        }
        StringBuffer message = new StringBuffer();
        int errors = (offers.size() * reportTypeList.size() - uploadCount.getCountFile());
        long duration = System.currentTimeMillis()/1000L - startTime;
        message.append("Files upload to " + tableType.getTableName() + " finished successfully! " +
                getUploadCountMessage(uploadCount,duration,errors));
        logger.info(message.toString());
        return uploadCount;
    }

    private StringBuffer getUploadCountMessage(UploadCount uploadCount, long duration, int errors) {
        return new StringBuffer ("Uploaded from " + uploadCount.getCountFile() + " csv files. " +
                "Originally in the csv file(s) were " + uploadCount.getCountTempTable() + " records. " +
                "Imported " + uploadCount.getCountInsert() + " rows. " +
                "Not valid Media source in: " + uploadCount.getCountMissingMediaSource() + " records. " +
                "Duplicates "+ uploadCount.getCountDuplicate() +" records. " +
                "Import duration: " + duration + " seconds. " +
                "Errors during import: " + errors);
    }

    private UploadCount importSingleFileFromAppsflyer(AppsflyerImportConfig importEntry, ReportType reportType, LocalDate fromDate, LocalDate toDate, AppsflyerTableType tableType) {
        long startTime = System.currentTimeMillis()/1000L;
        try {
            String fileNameFromDate;
            String fileNameToDate;
            StringBuffer url = new StringBuffer("https://hq.appsflyer.com/export/" + importEntry.getOffer_id() + "/" + reportType.getUrlName() + "/v4?api_token=" + importEntry.getToken());
            DateTimeFormatter formatterURL = DateTimeFormatter.ofPattern("yyyy-MM-dd'%20'HH'%3A'mm");
            DateTimeFormatter formatterFileTime = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            DateTimeFormatter formatterFileDate = DateTimeFormatter.ofPattern("yyyyMMdd");
            if (null == fromDate) {
                LocalDateTime fromDateTime = null;
                Timestamp timestamp = dao.getLastEventTimeForOfferId(importEntry.getOffer_id(), reportType, tableType);
                if (null != timestamp) {
                    fromDateTime = timestamp.toLocalDateTime();
                }
                logger.info("Last event time for " + reportType.name() + " is " + fromDateTime);
                if (fromDateTime != null && fromDateTime.until(LocalDateTime.now(), ChronoUnit.DAYS) < 7) {
                    fileNameFromDate = fromDateTime.format(formatterFileTime);
                    fileNameToDate = LocalDateTime.now().format(formatterFileTime);
                    url.append("&from=" + fromDateTime.format(formatterURL) + "&to=" + LocalDateTime.now().format(formatterURL));
                } else {
                    logger.warn("Last event time not found or too old");
                    fileNameFromDate = LocalDate.now().minusDays(7).format(formatterFileDate);
                    fileNameToDate = LocalDate.now().format(formatterFileDate);
                    url.append("&from=" + LocalDate.now().minusDays(7) + "&to=" + LocalDate.now());
                }
            } else {
                fileNameFromDate = fromDate.format(formatterFileDate);
                fileNameToDate = toDate.format(formatterFileDate);
                url.append("&from=" + fromDate + "&to=" + toDate);
            }
            logger.info("Downloading csv from url: " + url);

            File file = FileProcessing.saveToFile("appsflyer_backup_" + importEntry.getOffer_id() + "_" + fileNameFromDate + "_" + fileNameToDate + "_" + reportType.getWebParam() + "_" + tableType.getTableName() + ".csv", url.toString());
            if (null == file) {
                return new UploadCount();
            }
            ZipFile zipFile = ZipUtil.zip(file);
            GoogleStorageProvider.uploadFileAndDelete(ImportCostSource.appsflyer+"/" + zipFile.getFile().getName(), "text/plain", zipFile.getFile(), bucketName);

            long duration = System.currentTimeMillis()/1000L - startTime;
            logger.info("Download finished. Duration: " + duration + "seconds");
            UploadCount uploadCount = uploadFileIntoDB(Collections.singletonList(file.getAbsolutePath()), file.getAbsolutePath(), reportType, importEntry, tableType);
            String importFinishedMsg = "Single file upload finished successfully! " +
                    getUploadCountMessage(uploadCount, duration, 0);
            logger.info(importFinishedMsg);
            return uploadCount;
        } catch (Exception e) {
            logger.error("Error export file from Appsflyer.", e);
            return new UploadCount();
        }
    }

    public UploadCount uploadFileIntoDB(Collection<String> paths, String fileName, ReportType reportType, AppsflyerImportConfig importEntry, AppsflyerTableType tableType) throws IOException, SQLException {
        long tempCountSummary = 0;
        int insertCountSummary = 0;
        long mediaSourceInvalidCount = 0;
        int counter = 0;
        for (String path : paths) {
            path = path.replaceAll("\\\\","\\\\\\\\"); //fix for windows path backslash
            counter++;
            logger.info("Importing file #" + counter + ", total files: "+ paths.size() +", file name:  " + fileName + ", local path: "   + path + "");
            logger.info("Validating file #" + counter + ", file name:  " + fileName + ", local path: "   + path + "");
            String sqlQuery;
            try {
                List<String> fileLines = getFirstFileLines(path, tableType==AppsflyerTableType.GENERAL?1000:1);
                sqlQuery = reportProcessor.prepareSQLforImport(fileLines, path, reportType, tableType);
            } catch (MandatoryColumnsMissingException exception) {
                continue;
            }

            logger.info("Truncating temp table");
            dao.truncateTempTable(reportType);

            logger.info("Importing file into temp table");
            long tempCount = dao.importDataFileIntoTempTable(sqlQuery);

            final int APPSFLYER_MAX_LIMIT = 200000;
            if (tempCount >= APPSFLYER_MAX_LIMIT) {
                logger.error(tempCount + " records was returned from appsflyer for offer: "+ importEntry.getOffer_id() + " File path: " + path );
            }

            int numberOfInvalidRecords = 0;
            if (tableType == AppsflyerTableType.GENERAL) {
                logger.info("Deleting non-valid records");
                dao.deleteRowsWithNullColumn(reportType, "media_source");
                double insertFailureRatio = ((double) numberOfInvalidRecords) / ((double) tempCount);
                logger.info("Deleted " + numberOfInvalidRecords + " invalid records out of " + tempCount + " in file " + path);

                if (insertFailureRatio > MAX_FAILED_RECORDS_RATIO) {
                    tempCountSummary += tempCount;
                    logger.error("Failure ratio " + insertFailureRatio + " is too high. Aborting import of file.");
                    continue;
                }
            }

            if (reportType == ReportType.IN_APP_EVENTS) {
                logger.info("Normalize event names");
                normalizeEventName();
            }

            logger.info("Copying temp table to permanent");
            int insertCount = dao.copyTempTableToPermanent(reportType, importEntry, tableType);

            logger.info("Deleting file from the disk");
            new File(path).delete();
            tempCountSummary+=tempCount;
            insertCountSummary+=insertCount;
            mediaSourceInvalidCount+=numberOfInvalidRecords;


            logger.info("Originally in the csv file: " + tempCount);
            logger.info("Inserted: " + insertCount);
            logger.info("Failed: " + (tempCount - insertCount));
        }

        for(String path : paths) {
            File file = new File(path);
            if(file.exists()) {
                if(!file.delete()) {
                    logger.error("Could not delete temporary file " + file.getAbsolutePath());
                }
            }
        }

        return new UploadCount(tempCountSummary, insertCountSummary, mediaSourceInvalidCount, paths.size());
    }

    private List<String> getFirstFileLines(String filePath, int linesNumber) {
        LineIterator lineIterator;
        try {
            lineIterator = FileUtils.lineIterator(new File(filePath));

            List<String> fileLines = new ArrayList<>(linesNumber);

            int i = 0;
            while (lineIterator.hasNext() && ++i <= linesNumber)
                fileLines.add(lineIterator.next());

            lineIterator.close();

            return fileLines;
        } catch (IOException e) {
            logger.error("getFirstFileLines error", e);
            return null;
        }
    }

    private void normalizeEventName() throws SQLException, IOException {
        String setting = dao.getSettings("event_normalization");
        if (setting != null && !setting.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            EventNormalization eventNormalization = mapper.readValue(setting, EventNormalization.class);
            if (eventNormalization != null) {
                for (EventNormalization.EventNormalizationEntry entry : eventNormalization.evetentForNormalization) {
                    StringBuilder replaceableNames = new StringBuilder();
                    for (String name : entry.replaceableNames) {
                        if (replaceableNames.length()>0) {
                            replaceableNames.append(" OR ");
                        }
                        if (!name.isEmpty()) {
                            replaceableNames.append("event_name = " + '"' + name + '"');
                        }
                    }
                    dao.setNormalizeEventName(entry.normalizedEventName, replaceableNames.toString());
                }
            }
        }
        dao.setNullableNormalizeEventNames();
    }
}
