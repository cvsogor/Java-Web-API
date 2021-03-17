package com.aditor.dashboard.spring.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import com.google.api.client.http.apache.ApacheHttpTransport;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;
import org.apache.http.NoHttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleStorageProvider {
    private static final String APPLICATION_NAME = "aditor";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    //private static final String TEST_FILENAME = "json-test.txt";
    private static Storage storageService;
    private static final Logger logger = LoggerFactory.getLogger(GoogleStorageProvider.class);

    private static Storage getService() throws IOException, GeneralSecurityException {
        if (null == storageService) {
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            if (credential.createScopedRequired()) {
                credential = credential.createScoped(StorageScopes.all());
            }
            //HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            ApacheHttpTransport httpTransport = new ApacheHttpTransport();

            storageService = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        }
        return storageService;
    }

    public static List<StorageObject> listBucket(String bucketName) {
        int retryCount = 5;
        Exception exception = null;
        while (retryCount > 0) {
            try {
                Storage client = getService();
                Storage.Objects.List listRequest = client.objects().list(bucketName);
                List<StorageObject> results = new ArrayList<>();
                Objects objects;
                do {
                    objects = listRequest.execute();
                    results.addAll(objects.getItems());
                    listRequest.setPageToken(objects.getNextPageToken());
                }
                while (null != objects.getNextPageToken());
                return results;
            } catch (Exception e) {
                exception = e;
                logger.warn("Error get list in google bucket " + bucketName, ". Reason: " + e.getMessage());
                retryCount--;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interruptedException) {
                    logger.error("InterruptedException during thread sleep", interruptedException);
                    return Collections.EMPTY_LIST;
                }
                logger.info("Trying again");
            }
        }
        logger.error("Error get list in google bucket " + bucketName, exception);
        return Collections.EMPTY_LIST;
    }

    public static Bucket getBucket(String bucketName) throws IOException, GeneralSecurityException {
        Storage client = getService();

        Storage.Buckets.Get bucketRequest = client.buckets().get(bucketName);
        // Fetch the full set of the bucket's properties (e.g. include the ACLs in the response)
        bucketRequest.setProjection("full");
        return bucketRequest.execute();
    }

    public static boolean uploadFileAndDelete(String fullFileName, String contentType, File file, String bucketName) {
        if (uploadStream(fullFileName, contentType, Util.getFileInputStream(file), bucketName)) {
            if(!file.delete()) {
                logger.error("Could not delete temporary file " + file.getAbsolutePath());
            }
            return true;
        }
        return false;
    }

    public static boolean uploadStream(String fullFileName, String contentType, InputStream stream, String bucketName) {
        if (stream == null) {
            return false;
        }
        int retryCount = 5;
        Exception exception = null;
        while (retryCount > 0) {
            try {
                InputStreamContent contentStream = new InputStreamContent(contentType, stream);
                StorageObject objectMetadata = new StorageObject()
                        // Set the destination object name
                        .setName(fullFileName)
                        // Set the access control list to publicly read-only
                        .setAcl(Arrays.asList(
                                new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

                // Do the insert
                Storage client = null;
                Storage.Objects.Insert insertRequest = null;

                    client = getService();
                    insertRequest = client.objects().insert(
                            bucketName, objectMetadata, contentStream);
                insertRequest.execute();
                return true;
            } catch (Exception e) {
                exception = e;
                logger.warn("Error upload to google bucket " + bucketName + ". Reason: " + e.getMessage());
                retryCount--;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interruptedException) {
                    logger.error("InterruptedException during thread sleep", interruptedException);
                    return false;
                }
                logger.info("Trying again");
            }
        }
        logger.error("Uploading to google bucket failed", exception);
        return false;
    }

    public static void deleteObject(String path, String bucketName) throws IOException, GeneralSecurityException {
        Storage client = getService();
        client.objects().delete(bucketName, path).execute();
    }
    public static Date getMaxDateInBucket(String bucket)
    {
        Date dateMax = null;
        //cooladata-t78zbi3w681c9a5jry96sedsj1lnsxcg //cooladata-gcx6jrm77gf4qvoyb9x9n15vdr2hh07u
        List<StorageObject> bucketContents = GoogleStorageProvider.listBucket(bucket);
        for (StorageObject object : bucketContents) {
            String s = object.getName();
            Pattern p = Pattern.compile("_[0-9]{4}-[0-9]{2}-[0-9]{2}[^_]");
            Matcher m = p.matcher(s);
            if (m.find()) {
                String d = m.group().replaceFirst("_", "");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = formatter.parse(d);
                } catch (ParseException e) {
                    logger.error("Parsing error in string " + d, e);
                }
                if (dateMax == null || (date != null && dateMax.getTime() < date.getTime())) {
                    dateMax = date;
                }
            }
        }
        return dateMax;
    }

}
