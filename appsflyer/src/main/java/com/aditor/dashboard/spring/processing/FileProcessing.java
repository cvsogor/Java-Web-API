package com.aditor.dashboard.spring.processing;

import com.aditor.dashboard.spring.model.ImportCostSource;
import com.aditor.dashboard.spring.util.GoogleStorageProvider;
import com.aditor.dashboard.spring.util.ZipUtil;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.aditor.dashboard.spring.util.ZipUtil.isZipFile;

/**
 * Created by dgordiichuk on 03.02.2016.
 */
@Repository
public class FileProcessing {
    private final static Logger logger = LoggerFactory.getLogger(CooladataProcessing.class);

    public static File saveToFile(String fileName, String url) {
        File file = null;
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                String s = IOUtils.toString(stream);
                s = s.replaceAll("[$]", "");
                file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
                FileUtils.writeStringToFile(file, s);
            } else {
                logger.error("Error during downloading file from URL: " + url + "\nError code: " + statusCode + "\nReason phrase: " + statusLine.getReasonPhrase());
                File errorFile = File.createTempFile("Error_"+ LocalDate.now()+"_",".txt");
                FileUtils.writeStringToFile(errorFile, IOUtils.toString(response.getEntity().getContent()));
                logger.error("Full error text saved to file: " + errorFile.getAbsolutePath());
                return null;
            }
        } catch (IOException e) {
            logger.error("saveToFile error: ", e);
        }
        return file;
    }

    public static Collection<String> saveFileToDisk(File file, byte[] fileData, ImportCostSource importSource) throws IOException, GeneralSecurityException {
        FileUtils.writeByteArrayToFile(file, fileData);
        String filePath = file.getAbsolutePath();
        if(isZipFile(file.getName())){
            String tempFolderPath = System.getProperty("java.io.tmpdir") + File.separator + importSource.name() + "_zip_extracted_" + System.currentTimeMillis();
            File extractedZipFolder = new File(tempFolderPath);
            extractedZipFolder.mkdir();
            ZipUtil.unzip(filePath, tempFolderPath);
            File[] filesToUpload = extractedZipFolder.listFiles();
            if (filesToUpload == null || filesToUpload.length == 0) {
                return Collections.emptyList();
            }
            Set<String> filePaths = Arrays.stream(filesToUpload).map(File::getAbsolutePath).collect(Collectors.toSet());
            return filePaths;
        }
        else {
            return Collections.singletonList(filePath);
        }
    }
}
