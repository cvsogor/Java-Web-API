package com.aditor.dashboard.spring.processing;

import com.aditor.dashboard.spring.Dao;
import com.aditor.dashboard.spring.model.CooladataExportEntry;
import com.aditor.dashboard.spring.model.MutableInteger;
import com.aditor.dashboard.spring.util.GoogleStorageProvider;
import com.aditor.dashboard.spring.util.GzipUtil;
import com.aditor.dashboard.spring.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dgordiichuk on 02.02.2016.
 */
@Repository
public class CooladataProcessing {

    private final static String bucketNameCooladata = "cooladata-t78zbi3w681c9a5jry96sedsj1lnsxcg";
    private final static Logger logger = LoggerFactory.getLogger(CooladataProcessing.class);

    @Autowired
    private Dao dao;

    public boolean exportToCooladataCommercials(StringBuffer message, MutableInteger exportedCount, String whereClause) {
        long startTime = System.currentTimeMillis()/1000L;
        List<CooladataExportEntry> cooladataExportEntryList = dao.selectCooladataExportList(whereClause, "");

        if (cooladataExportEntryList.isEmpty()) {
            String s = "Nothing to export";
            logger.warn(s);
            message.append(s);
            return true;
        }

        boolean result = exportToCooladataProcessing(cooladataExportEntryList, exportedCount, new MutableInteger(0), message);
        if (!result) {
            return false;
        }
        Set<Integer> ids = cooladataExportEntryList.stream().map(CooladataExportEntry::getId).collect(Collectors.toSet());
        dao.setFirstExportTime(ids);
        long duration = System.currentTimeMillis()/1000L - startTime;
        String durationString = "Duration: " + duration + "seconds";
        logger.info("Finish export commercials to cooladata. " + durationString);
        message.append(durationString);
        return true;
    }

    private boolean exportToCooladataProcessing (List<CooladataExportEntry> cooladataExportEntryList, MutableInteger exportedCount, MutableInteger lastExportedId, StringBuffer message) {
        int size = cooladataExportEntryList.size();
        logger.info("Exporting to Cooladata " + size + " rows.");
        lastExportedId.set(cooladataExportEntryList.get(size-1).getId());
        Collections.sort(cooladataExportEntryList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(cooladataExportEntryList);
        } catch (JsonProcessingException e) {
            String errorString = "Error during Json Processing\n";
            message.insert(0,errorString);
            logger.error(errorString,e);
            return false;
        }
        jsonString=jsonString.substring(1,jsonString.length()-1);
        jsonString=jsonString.replaceAll("},","}\r\n");
        byte[] gzipedData;
        File file;
        try {
            gzipedData = GzipUtil.compress(jsonString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_");
            file = File.createTempFile("events_"+ LocalDate.now().format(formatter), ".json.gz");
            FileUtils.writeByteArrayToFile(file, gzipedData);
        } catch (IOException e) {
            String errorString = "Error during gziping json\n";
            message.insert(0,errorString);
            logger.error(errorString,e);
            return false;
        }
        boolean result = GoogleStorageProvider.uploadFileAndDelete("" + file.getName(), "text/plain", file, bucketNameCooladata);

        if (!result) {
            String errorString = "Error during export to cooladata\n";
            message.insert(0,errorString);
            logger.error(errorString);
            return false;
        }

        exportedCount.add(size);
        String info = "Exported to Cooladata: " + size + " rows. Last imported id = " + lastExportedId;
        logger.info(info);
        message.append(info +'\n');
        return true;
    }

    public boolean exportToCooladataGeneral (StringBuffer message, MutableInteger exportedCount) {
        if (!Util.synchronizeCommercials()) {
            return false;
        }
        long startTime = System.currentTimeMillis()/1000L;
        String idString;
        try {
            idString = dao.getSettings("last_imported_to_cooladata");
        } catch (SQLException e) {
            String errorString = "Error during reading settings\n";
            message.insert(0,errorString);
            logger.error(errorString,e);
            return false;
        }
        int id = 0;
        if (null != idString) {
            id = Integer.parseInt(idString);
        }
        logger.info("Last imported ID = " + id);
        int limit = 10000;
        List<CooladataExportEntry> cooladataExportEntryList = dao.selectNewEntry(id, limit);

        while (!cooladataExportEntryList.isEmpty()) {
            MutableInteger lastExportedId = new MutableInteger(0);
            boolean result = exportToCooladataProcessing(cooladataExportEntryList, exportedCount, lastExportedId, message);
            if (!result) {
                return false;
            }
            Set<Integer> ids = cooladataExportEntryList.stream().map(CooladataExportEntry::getId).collect(Collectors.toSet());
            dao.setFirstExportTime(ids);
            dao.setLastExportedEntry(lastExportedId.intValue());
            cooladataExportEntryList = dao.selectNewEntry(lastExportedId.intValue(), limit);
        }
        long duration = System.currentTimeMillis()/1000L - startTime;
        String durationString = "Duration: " + duration + "seconds";
        logger.info("Finish export to cooladata. " + durationString);
        message.append(durationString);
        return true;
    }
}
