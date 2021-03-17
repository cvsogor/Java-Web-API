package com.aditor.dashboard.spring;

import com.aditor.dashboard.spring.model.*;
import com.aditor.dashboard.spring.processing.AppsflyerProcessing;
import com.aditor.dashboard.spring.processing.CooladataProcessing;
import com.aditor.dashboard.spring.util.*;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.aditor.dashboard.spring.util.ZipUtil.isZipFile;

import org.json.CDL;

@Controller
public class DashboardController
{
    @Autowired
    private Dao dao;
    @Autowired
    private ReportGenerator reportGenerator;
    @Autowired
    private CooladataProcessing cooladataProcessing;
    @Autowired
    private AppsflyerProcessing appsflyerProcessing;

    final String  bucketName = "non-rtb-backup";
    String applovinTable = "applovin_costs_report_new";
    String applovinTable2 = "applovin_costs_report2";
    String applovinTable3 = "applovin_costs_report3";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        return "index";
    }

    @RequestMapping(value="/ImportCostsFromAppLovin", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromAppLovin() {
        String file = Util.importCosts(ImportCostSource.applovin.name(), FileFormat.csv.name(), "yyyy-MM-dd", "https://r.applovin.com/report?api_key=74KiS7w7i8D6lWvBh_VSp-oITMtKQiHTnbGEkALQJZX25QDnQkEEh0WENNZp5rMsRc5AoQa5_774TLsz44Bm5i&start=", "&end=", "&format=csv&report_type=advertiser&columns=day,campaign,country,impressions,clicks,ctr,conversions,conversion_rate,cost");//day,impressions,clicks,ctr,conversions,conversion_rate,average_cpa,average_cpc,campaign,cost
        long result = dao.importCostsFile(file, applovinTable);
        file = Util.importCosts(ImportCostSource.applovin2.name(), FileFormat.csv.name(), "yyyy-MM-dd", "https://r.applovin.com/report?api_key=poQsC8S8tqTC00RwxJZ6PRSZGDhsrXaeK0UX9yg0m8Dn9fh4zFBNhexzsdphAOKUOq1TnhTWqnIzQ-2OEDmwsh&start=", "&end=", "&format=csv&report_type=advertiser&columns=day,campaign,country,impressions,clicks,ctr,conversions,conversion_rate,cost");
        long result2 = dao.importCostsFile(file, applovinTable2);
        file = Util.importCosts(ImportCostSource.applovin3.name(), FileFormat.csv.name(), "yyyy-MM-dd", "https://r.applovin.com/report?api_key=4PpTC3cNSzozELungXEmevrg0GDikAToZMLnPy9twxq9HixyE9it3XX3ommWxJIVfdG4aQxVLmfdSaahkHZnq5&start=", "&end=", "&format=csv&report_type=advertiser&columns=day,campaign,country,impressions,clicks,ctr,conversions,conversion_rate,cost");
        long result3 = dao.importCostsFile(file, applovinTable3);
        return String.format("Imported %1$d rows from AppLovin, %2$d rows from AppLovin2, %3$d rows from AppLovin3", result, result2, result3);
    }

    @RequestMapping(value="/ImportFromAdColony", method=RequestMethod.POST)
    public @ResponseBody String importFromAdColony() {
        String file = Util.importCosts(ImportCostSource.adcolony.name(), FileFormat.csv.name(), "MMddyyyy","https://clients.adcolony.com/api/v2/advertiser_summary?user_credentials=3uhRGyt7KD8xLrE7DqX&date=","&end_date=","&format=csv&group_by=ad_group&date_group=day");
        long result = dao.importCostsFile(file, "adcolony_costs_report");
        return "AdColony import finished";
    }

    @RequestMapping(value="/ImportFromHeyzap", method=RequestMethod.POST)
    public @ResponseBody String importFromHeyzap() {
        String file = Util.importCosts(ImportCostSource.heyzap.name(), FileFormat.csv.name(), "yyyy-MM-dd","https://developers.heyzap.com/api/campaigns/stats?key=bb22ead00ecbe32589fd7c8923ebbdb681c10ca5d82fcbb230bcc4ffd3a58b60&start_date=","&end_date=","&format=csv");
        long result = dao.importCostsFile(file, "heyzap_costs_report");
        return "Heyzap import finished";
    }

    @RequestMapping(value="/ImportCostsFromVungle", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromVungle()
    {
        String file = importVungle(ImportCostSource.vungle.name(), FileFormat.json.name(), "yyyy-MM-dd");
        long result = dao.importCostsFile(file, "vungle_costs_report");
        return "Vungle import finished";
    }

    public String importVungle(String bucket, String fileFormat, String dateFormat)
    {
        ImportParam param = new ImportParam();
        Util.preImport(fileFormat, dateFormat, bucket, param);
        File file = importVungleToFile(param);
        Util.sendByMailSaveToStorage(file, bucket);
        return file.getAbsolutePath();
    }

    @RequestMapping(value="/importCostsFromVoluum", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromVoluum()
    {
        String file = importVoluum(ImportCostSource.voluum.name(), FileFormat.json.name(), "yyyy-MM-dd");
        //long result = dao.importCostsFile(file, "voluum_costs_report");
        return "Voluum import finished";
    }
    public String importVoluum(String bucket, String fileFormat, String dateFormat)
    {
        ImportParam param = new ImportParam();
        Util.preImport(fileFormat, dateFormat, bucket, param);
        File file = importVoluumToFile(param);
        Util.sendByMailSaveToStorage(file, bucket);
        return file.getAbsolutePath();
    }

    public File importVungleToFile(ImportParam param ) //TODO: improve data aggregation after Vungle fix reporting API
    {
        String url = "https://ssl.vungle.com/api/campaigns?key=fdc0e8306eb993610005a9097b5e61f4";
        String response = String.format("{\"campaigns\":%1$s}", Util.getURLResponse(url, Collections.emptyMap()));
        response = response.replace("}{","},{");
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("campaigns");
        String csv = CDL.toString(arr);
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + FilenameUtils.removeExtension(param.fileName) + ".csv");
        try
        {
            FileUtils.writeStringToFile(file, csv);
        }
        catch(Exception e)
        {
            logger.error("Vungle write to file error", e);
        }
        return file;
    }

    public File importVoluumToFile(ImportParam param )
    {
        File file = null;
        String report = "";
        String username = "noam.g@aditor.com";
        String password = "Aditor321";
        String url = "https://security.voluum.com/login";
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Basic " + Base64.getEncoder().encodeToString((username+":"+password).getBytes()));
        try
        {
            JSONObject jsonObject = new JSONObject(Util.getURLResponse(url, header));
            url = "https://reports.voluum.com/report?from=" + param.dateMax + "&to=" + param.today + "&tz=Europe%2FWarsaw&sort=visits&direction=desc&columns=campaignName&columns=visits&columns=clicks&columns=conversions&columns=revenue&columns=cost&groupBy=campaign";
            header.clear();
            header.put("cwauth-token", jsonObject.getString("token"));
            report = Util.getURLResponse(url, header);

            JSONObject obj = new JSONObject(report);
            JSONArray arr = obj.getJSONArray("rows");
            String csv = CDL.toString(arr);

            file = new File(System.getProperty("java.io.tmpdir") + File.separator + FilenameUtils.removeExtension(param.fileName) + ".csv");
            FileUtils.writeStringToFile(file, csv);
        }
        catch(Exception e)
        {
            logger.error("Voluum http response error", e);
        }

        return file;
    }


    public File importSuperSonicToFile(String username, String  password, ImportParam param )
    {
        String json = String.format("{\"username\": \"%1$s\", \"password\": \"%2$s\"}", username, password);
        String url = "https://platform.supersonic.com/partners/auth/login";
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Referer", "https://platform.supersonic.com/partners/login");
        httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
        HttpEntity entity = new ByteArrayEntity(json.getBytes());
        httppost.setEntity(entity);
        JSONObject jsonObject = null;
        try
        {
            HttpResponse response = httpclient.execute(httppost);
            jsonObject = new JSONObject(IOUtils.toString(response.getEntity().getContent()));
        }
        catch(IOException e)
        {
            logger.error("Supersonic http response error", e);
        }

        String csvUrl = String.format("https://platform.supersonic.com/api/rest/v1/partners/statistics.csv?fromDate=%1$s&toDate=%2$s&breakdowns[]=campaign&campaignStatus=any&app=any&authorization=%3$s", param.dateMax, param.today, jsonObject.getString("token"));
        String campaigns = Util.getURLResponse(csvUrl, Collections.emptyMap());
        String[] rows = campaigns.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < rows.length - 1; i++)
        {
            String[] cols = rows[i].split(",");
            int campaignId = Integer.parseInt(cols[0]);
            String reportURL = String.format("https://platform.supersonic.com/api/rest/v1/partners/statistics.csv?fromDate=%1$s&toDate=%2$s&breakdowns[]=campaign&breakdowns[]=date&breakdowns[]=country&campaignId[]=%3$s&campaignStatus=any&app=any&authorization=%4$s", param.dateMax, param.today, campaignId, jsonObject.getString("token"));
            String report = Util.getURLResponse(reportURL, Collections.emptyMap());
            String[] lines = report.split("\n");
            if(i == 1)
            {
                sb.append(lines[0]);
                sb.append("\n");
            }
            for (int j = 1; j < lines.length - 1; j++)
            {
                sb.append(lines[j]);
                sb.append("\n");
            }
        }
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + param.fileName);
        try
        {
            FileUtils.writeStringToFile(file, sb.toString());
        }
        catch(IOException e)
        {
            logger.error("Supersonic write to file error", e);
        }
        return file;
    }
    public String importSuperSonic(String username, String password, String bucket, String fileFormat, String dateFormat)
    {
        ImportParam param = new ImportParam();
        Util.preImport(fileFormat, dateFormat, bucket, param);
        File file = importSuperSonicToFile(username, password, param);
        Util.sendByMailSaveToStorage(file, bucket);
        return file.getAbsolutePath();
    }
    @RequestMapping(value="/importCostsFromSuperSonic", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromSuperSonic()
    {
        String file = importSuperSonic("aditor@aditor.com", "superaditor", ImportCostSource.supersonic.name(), FileFormat.csv.name(), "yyyy-MM-dd");
        long result = dao.importCostsFile(file, "supersonic_costs_report");
        return "SuperSonic import finished";
    }

    @RequestMapping(value="/importCostsFromUnityAds", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromUnityAds()
    {
        String file = Util.importCosts(ImportCostSource.applifer.name(), FileFormat.csv.name(), "yyyy-MM-dd", "http://gameads-admin.applifier.com/stats/acquisition-api?apikey=5a197d9ef663823947de150ef201905a62880fbcc97b2af3482f3b84e82de999&start=", "&end=", "");
        long result = dao.importCostsFile(file, "unityads_costs_report");
        return "Unity Ads import finished";
    }

    @RequestMapping(value="/ImportCostsFromMediaSources", method=RequestMethod.POST)
    public @ResponseBody String importCostsFromMediaSources() {
        String appLovin = importCostsFromAppLovin();
        String adColony = importFromAdColony();
        String heyzap = importFromHeyzap();
        String vungle = importCostsFromVungle();
        String unityAds = importCostsFromUnityAds();
        String superSonic = importCostsFromSuperSonic();
        return adColony + " " + heyzap + " " + appLovin + " " + vungle + " " + superSonic + " " + unityAds ;
    }

    @RequestMapping(value="/ImportAppLovinCostsFromFile", method=RequestMethod.POST)
    public @ResponseBody String importCosts(@RequestParam("costsfile") MultipartFile file)
    {
        if (!file.isEmpty())
        {
            try {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                long startTime = System.currentTimeMillis()/1000L;

                uploadCostsFileIntoDB(fileName, bytes);

                long duration = System.currentTimeMillis()/1000L - startTime;
                String importFinishedMsg = "File upload finished successfully! " +
                        "Import duration: " + duration + " seconds. ";
                logger.info(importFinishedMsg);
                return importFinishedMsg;
            } catch (Exception e) {
                System.out.println(e.toString());
                logger.error("File upload failed, error:" , e);
                return "File upload failed, error:  " + e.getMessage();
            }
        } else {
            return "File upload failed, file is empty";
        }
    }

    private void uploadCostsFileIntoDB(String fileName, byte[] bytes) throws IOException, GeneralSecurityException {
        Collection<String> paths = saveFileToDisk(fileName, bytes);

        int counter = 0;
        for (String path : paths) {
            counter++;
            logger.info("Importing file #" + counter + ", total files: "+ paths.size() +", file name:  " + fileName + ", local path: "   + path + "");
            logger.info("Importing file into temp table");

            FileInputStream file = new FileInputStream(path);
            String s = IOUtils.toString(file);
            s = s.replaceAll("[$]","");
            try
            {
                FileWriter fw = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(s);
                bw.close();
            }
            catch(Exception e)
            {
                logger.error("Write costs to file error", e);
            }

            long tempCount = dao.importCostsFile(path, applovinTable);

            logger.info("Deleting file from the disk");
            new File(path).delete();

            logger.info("Originally in the csv file: " + tempCount);
        }
    }

    @RequestMapping(value="/uploadFromWeb", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> handleWebUpload(@RequestParam(value = "from_date", required = true) String fromDateStr,
                                                @RequestParam(value = "to_date", required = true) String toDateStr,
                                                @RequestParam(name = "radios", required = true) String radious,
                                                @RequestParam(name = "table", required = true) String table,
                                                @RequestParam(value = "offer_id", required = false) String offerId){
        try {
            logger.info("Request parameters: fromDateStr=" + fromDateStr + " toDateStr=" + toDateStr + " radious=" + radious + " table=" + table + " offer_id=" + offerId);
            LocalDate fromDate = LocalDate.parse(fromDateStr);
            LocalDate toDate = LocalDate.parse(toDateStr);
            StringBuffer message = new StringBuffer();
            List<ReportType> reportTypeList = new ArrayList<>();
            ReportType reportType = null;
            try {
                reportType = ReportType.findByWebParam(radious);
            } catch (IllegalArgumentException ignore) {}
            if (null == reportType) {
                reportTypeList.add(ReportType.INSTALL);
                reportTypeList.add(ReportType.IN_APP_EVENTS);
            } else {
                reportTypeList.add(reportType);
            }

            List<AppsflyerImportConfig> offers = Collections.EMPTY_LIST;;
            List<AppsflyerImportConfig> offersFull = Collections.EMPTY_LIST;
            if (null == offerId || offerId.trim().isEmpty()) {
                if (table.trim().equals("general")) {
                    offers = dao.getAppsflyerImportConfig();
                } else {
                    offersFull = dao.getAppsflyerImportConfigFull();
                }
            } else {
                if (table.trim().equals("general")) {
                    offers = dao.selectOneOfferFromAppsflyerImportConfig(offerId.trim());
                } else {
                    offersFull = dao.selectOneOfferFromAppsflyerImportConfigFull(offerId.trim());
                }
            }
            boolean result = appsflyerProcessing.importFromAppsflyer(fromDate, toDate, message, reportTypeList, offers, offersFull);
            if (!result) {
                String errorString = "Error during import from Appsflyer";
                logger.error(errorString);
                return new ResponseEntity<>(errorString, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (table.trim().equals("general")) {
                if (!Util.synchronizeCommercials()) {
                    return new ResponseEntity<>("Error during synchronize commercials", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                exportToCooladataRequest(null);
            }
            return new ResponseEntity<>(message.toString(), HttpStatus.OK);
        } catch (Exception e) {
            String errorString = "Unknown error during uploadFromWeb";
            logger.error(errorString,e);
            return new ResponseEntity<>(errorString.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam(value = "advertiser", required = false) String advertiser,
                                                 @RequestParam(value = "offer_name", required = false) String offer_name,
                                                 @RequestParam(value = "offer_id") String offer_id,
                                                 @RequestParam(value = "offer_url", required = false) String offer_url,
                                                 @RequestParam(value = "offer_preview_url", required = false) String offer_preview_url,
                                                 @RequestParam(value = "os", required = false) String os,
                                                 @RequestParam(value = "platform_name", required = false) String platform_name,
                                                 @RequestParam(value = "platform_type", required = false) String platform_type,
                                                 @RequestParam(value = "report_type", required = false) String report_type,
                                                 @RequestParam(value = "business_model", required = false) String business_model,
                                                 @RequestParam(value = "commission", required = false) Double commission,
                                                 @RequestParam("file") MultipartFile file){


        logger.info("Request parameters: advertiser=" + advertiser + ", offer_name=" + offer_name +
                ", offer_id=" + offer_id + ", offer_url=" + offer_url + ", offer_preview_url=" + offer_preview_url + ", os=" + os +
                ", platform_name=" + platform_name + ", platform_type=" + platform_type + ", report_type=" + report_type +
                ", business_model=" + business_model + ", commission=" + commission );


        if (!file.isEmpty()) {
            try {
                Collection<String> paths = saveFileToDisk(file.getOriginalFilename(), file.getBytes());
                long startTime = System.currentTimeMillis()/1000L;
                ReportType reportType = ReportType.findByWebParam(report_type);
                AppsflyerImportConfig importEntry = new AppsflyerImportConfig(advertiser, offer_id, offer_name,
                        offer_url, offer_preview_url, os, platform_name, platform_type, business_model, commission);
                UploadCount uploadCount = appsflyerProcessing.uploadFileIntoDB(paths, file.getOriginalFilename(), reportType, importEntry, AppsflyerTableType.GENERAL);
                if (!Util.synchronizeCommercials()) {
                    return "Error during synchronize commercials";
                }
                exportToCooladataRequest(null);

                long duration = System.currentTimeMillis()/1000L - startTime;
                String importFinishedMsg = "File upload finished successfully! " +
                        "Uploaded from " + uploadCount.getCountFile() + " csv file(s). " +
                        "Originally in the csv file(s) were " + uploadCount.getCountTempTable() + " records. " +
                        "Imported " + uploadCount.getCountInsert() + " rows. " +
                        "Not valid Media source in: " + uploadCount.getCountMissingMediaSource() + " records. " +
                        "Duplicates "+ uploadCount.getCountDuplicate() +" records. " +
                        "Import duration: " + duration + " seconds. ";
                logger.info(importFinishedMsg);


                return importFinishedMsg;
            } catch (Exception e) {
                String errorString = "File upload failed, error: ";
                logger.error(errorString, e);
                return errorString + e.getMessage();
            }
        } else {
            String warningString = "File upload failed, file is empty";
            logger.warn(warningString);
            return warningString;
        }
    }

    @RequestMapping(value="/generateReport", method=RequestMethod.GET)
    public void generateReport(
                               @RequestParam(value = "from_date") String fromDateStr,
                               @RequestParam(value = "to_date") String toDateStr,
                               @RequestParam(value = "offer_name") String offerName,
                               @RequestParam(value = "country") String country,
                               @RequestParam(value = "agency") String agency,
                               @RequestParam(value = "media_source") String mediaSource,
                               @RequestParam(value = "campaign") String campaign,
                               @RequestParam(value = "site_id") String siteId,
                               @RequestParam(value = "report_name") String reportName,

                               HttpServletResponse response) throws IOException, ParseException {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = dateFormat.parse(fromDateStr);
            Date toDate = dateFormat.parse(toDateStr);



            //"af_ftd","af_purchase"

            String report;


            if(reportName.equals("uaba")){

                report = reportGenerator.generateReportSecond(fromDate, toDate, offerName.trim(), country.trim(), agency.trim(), mediaSource.trim(), campaign.trim(), siteId.trim());


            }else if (reportName.equals("advanced-uaba")) {

                report = reportGenerator.generateReportThird(fromDate, toDate, offerName.trim(), country.trim(), agency.trim(), mediaSource.trim(), campaign.trim(), siteId.trim());


            } else {
                report = reportGenerator.generateReportFirst(fromDate, toDate, offerName.trim(), country.trim(), agency.trim(), mediaSource.trim(), campaign.trim(), siteId.trim());
            }
            response.setContentType("text/csv;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=report.csv");
            response.getWriter().print(report);
        }catch (Exception ex){
            logger.error("Error processing report", ex);
            response.getWriter().print("Error processing report, see logs");
            throw new RuntimeException(ex);
        }
    }

    @RequestMapping(value="/generateOptimizedReport", method=RequestMethod.GET)
    public void generateOptimizedReport(
            @RequestParam(value = "from_date") String fromDateStr,
            @RequestParam(value = "to_date") String toDateStr,

            HttpServletResponse response) throws IOException, ParseException {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = dateFormat.parse(fromDateStr);
            Date toDate = dateFormat.parse(toDateStr);

            String report = reportGenerator.generateOptimizedReport(fromDate, toDate);

            response.setContentType("text/csv;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=report.csv");
            response.getWriter().print(report);
        }catch (Exception ex){
            logger.error("Error processing report", ex);
            response.getWriter().print("Error processing report, see logs");
            throw new RuntimeException(ex);
        }
    }

    private Collection<String> saveFileToDisk(String fileName, byte[] fileData) throws IOException, GeneralSecurityException {
        File file = File.createTempFile(FilenameUtils.removeExtension(fileName), '.' + FilenameUtils.getExtension(fileName));
        FileUtils.writeByteArrayToFile(file, fileData);

        String filePath = file.getAbsolutePath();
        if(isZipFile(fileName)){
            String tempFolderPath = System.getProperty("java.io.tmpdir") + File.separator + "appsflyer_zip_extracted_" + System.currentTimeMillis();
            File extractedZipFolder = new File(tempFolderPath);
            extractedZipFolder.mkdir();
            ZipUtil.unzip(filePath, tempFolderPath);
            File[] filesToUpload = extractedZipFolder.listFiles();
            if (filesToUpload.length == 0) {
                return Collections.emptyList();
            }
            Set<String> filePaths = Arrays.stream(filesToUpload).map(e -> e.getAbsolutePath()).collect(Collectors.toSet());
            GoogleStorageProvider.uploadStream(ImportCostSource.appsflyer+"/" + file.getName(), "text/plain", new FileInputStream(file.getAbsolutePath()), bucketName);
            if(!file.delete()) {
                logger.error("Could not delete temporary file " + file.getAbsolutePath());
            }
            return filePaths;
        }
        else
        {
            ZipFile zipFile = ZipUtil.zip(file);
            GoogleStorageProvider.uploadStream(ImportCostSource.appsflyer+"/" + zipFile.getFile().getName(), "text/plain", new FileInputStream(zipFile.getFile().getAbsolutePath()), bucketName);
            if(!zipFile.getFile().delete()) {
                logger.error("Could not delete temporary file " + zipFile.getFile().getAbsolutePath());
            }
            return Arrays.asList(filePath);
        }
    }

    @RequestMapping(value="/exportToCooladataWeb", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> exportToCooladataRequestWeb(@RequestParam(name = "where") String where) {
        return exportToCooladataRequest(where);
    }

    @RequestMapping(value="/exportToCooladata", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> exportToCooladataRequest(@RequestBody (required = false) String queryPart) {
        try {
            logger.info("Export to cooladata POST request receive");
            logger.info("RequestBody: " + queryPart);
            long startTime = System.currentTimeMillis() / 1000L;
            StringBuffer message = new StringBuffer();
            MutableInteger exportedCount = new MutableInteger(0);

            boolean result;
            if (null == queryPart || queryPart.isEmpty()) {
                result = cooladataProcessing.exportToCooladataGeneral(message, exportedCount);
            } else {
                result = cooladataProcessing.exportToCooladataCommercials(message, exportedCount, queryPart);
            }
            if (!result) {
                String errorString = "Error during export to cooladata";
                logger.error(errorString);
                message.insert(0,errorString);
                return new ResponseEntity<>(errorString, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            long duration = System.currentTimeMillis() / 1000L - startTime;
            String durationString = "Duration: " + duration + "seconds";
            logger.info("Finish export to cooladata post request. " + durationString);
            message.append(durationString);
            return new ResponseEntity<>(message.toString(), HttpStatus.OK);
        } catch (Exception e) {
            String errorString = "Unknown error during export to cooladata";
            logger.error(errorString, e);
            return new ResponseEntity<>(errorString.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/uploadFromWebAndExportToCooladata", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> uploadAndExport() {
        try {
            long startTime = System.currentTimeMillis() / 1000L;
            logger.info("Start upload From Web And Export To Cooladata");
            List<AppsflyerImportConfig> offers = dao.getAppsflyerImportConfig();
            //TODO remove
            List<AppsflyerImportConfig> offersFull = Collections.EMPTY_LIST;
//            List<AppsflyerImportConfig> offersFull = dao.getAppsflyerImportConfigFull();
            StringBuffer message = new StringBuffer();
            boolean result = appsflyerProcessing.importFromAppsflyer(null, null, message, Arrays.asList(ReportType.INSTALL, ReportType.IN_APP_EVENTS), offers, offersFull);
            if (!result) {
                String errorString = "Error during import from appsflyer\n";
                logger.error(errorString);
                message.insert(0,errorString);
                return new ResponseEntity<>(message.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            MutableInteger exportedCount = new MutableInteger(0);
            result = cooladataProcessing.exportToCooladataGeneral(message, exportedCount);
            if (!result) {
                String errorString = "Error during export to cooladata\n";
                logger.error(errorString, new RuntimeException(errorString));
                message.insert(0,errorString);
                return new ResponseEntity<>(message.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            long duration = System.currentTimeMillis() / 1000L - startTime;
            logger.info("upload From Web And Export To Cooladata finished. Duration: " + duration + "seconds");
            return new ResponseEntity<>(message.toString(), HttpStatus.OK);
        } catch (Exception e) {
            String errorString = "Unknown error during import from appsflyer or export to cooladata";
            logger.error(errorString, e);
            return new ResponseEntity<>(errorString.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
