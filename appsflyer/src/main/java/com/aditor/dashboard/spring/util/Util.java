package com.aditor.dashboard.spring.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.aditor.dashboard.spring.ImportParam;
import com.aditor.dashboard.spring.model.ImportCostSource;
import com.aditor.dashboard.spring.processing.CooladataProcessing;
import com.aditor.dashboard.spring.processing.FileProcessing;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by KSogor on 29/02/2016.
 */

public class Util
{
    private final static Logger logger = LoggerFactory.getLogger(CooladataProcessing.class);

    public static String importCosts(String bucket, String fileFormat, String dateFormat, String part1, String part2, String part3)
    {
        logger.info("Start " + bucket + "import");
        long startTime = System.currentTimeMillis() / 1000L;

        ImportParam param = new ImportParam();
        preImport(fileFormat, dateFormat, bucket, param);
        File file = Util.importCostsToFile(part1, part2, part3, param);
        sendByMailSaveToStorage(file, bucket);

        long duration = System.currentTimeMillis() / 1000L - startTime;
        logger.info(bucket + " import finished successful. Duration: " + duration + "seconds");

        return file.getAbsolutePath();
    }
    public static String getURLResponse(String url, Map<String,String> header)
    {
        String s = "";
        try
        {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(url);
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpget.addHeader(entry.getKey(),entry.getValue());
            }
            HttpResponse response = httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode >= 200 && statusCode < 300)
            {
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                s = IOUtils.toString(stream);
            }
        }
        catch (IOException e)
        {
            logger.error("getURLResponse error", e);
        }
        return s;
    }

    public static void preImport(String fileFormat, String dateFormat, String bucket, ImportParam param)
    {
        Date maxDate = GoogleStorageProvider.getMaxDateInBucket(bucket);
        if (maxDate == null) {
            logger.error("Unable to define the maximum date from " + bucket + " bucket");
            return ;
        }
        param.fileName = Util.getFileName(maxDate, bucket, fileFormat);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        param.today = df.format(new Date());
        param.dateMax = df.format(maxDate);
    }

    public static void sendByMailSaveToStorage(File file, String bucket)
    {
        ZipFile zipFile = ZipUtil.zip(file);
        GoogleStorageProvider.uploadFileAndDelete(zipFile.getFile().getName(), "application/x-zip-compressed", zipFile.getFile(), bucket);

        if(bucket.contains(ImportCostSource.applovin.name()))
        {
            bucket = ImportCostSource.applovin.name(); // fix for cooladata
        }
        new MailSender().SendMail("notification@aditor.com", "Krokodil1234", "aditorcooladata@gmail.com", "cost_" + bucket, "", file.getAbsolutePath());
        //new MailSender().SendMail("notification@aditor.com", "Krokodil1234", "cvsogor@gmail.com", "cost_" + bucket, "", file.getAbsolutePath());
        //zipFile.getFile().delete();
        //file.delete();
    }

    public static File importCostsToFile(String part1, String part2, String part3, ImportParam param )
    {
        String url = part1 + param.dateMax + part2 + param.today + part3;
        File file = FileProcessing.saveToFile(param.fileName, url);
        if (file == null) {
            logger.error("Unable to download file");
            return null;
        }
        return file;
    }

    public static String getFileName(Date maxDate, String bucket, String fileFormat)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return "costs_backup_" + bucket + "_" + df.format(maxDate) + "_" + df.format(new Date()) + "." + fileFormat;
    }
    public static FileInputStream getFileInputStream(File file)
    {
        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            logger.error("FileNotFoundException: ", e);
        }
        return stream;
    }

    public static boolean synchronizeCommercials() {
        long startTime = System.currentTimeMillis()/1000L;
        logger.info("Start synchronize commercial.");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String updateResult = restTemplate.postForObject("http://localhost/commercials/commercials/daily-update-sync", null, String.class);
            if(!updateResult.equals("success")) {
                logger.error("real_cost / real_revenue values update was not performed with following status: " + updateResult);
            }
        } catch (RestClientException restClientException) {
            logger.error("RestClient encounreted following exception " +
                    "during attempt to update real_cost/real_revenue values", restClientException);
            return false;
        }
        long duration = System.currentTimeMillis()/1000L - startTime;
        String durationString = "Duration: " + duration + "seconds";
        logger.info("Finish synchronize commercial. " + durationString);
        return true;
    }

}
