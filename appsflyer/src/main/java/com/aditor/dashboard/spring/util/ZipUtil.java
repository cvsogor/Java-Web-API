package com.aditor.dashboard.spring.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by art on 12/20/15.
 */
public class ZipUtil
{
    private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    public static void unzip(String sourcePath, String destinationPath)
    {
        try
        {
            ZipFile zipFile = new ZipFile(sourcePath);
            zipFile.extractAll(destinationPath);
        }
        catch (ZipException e)
        {
            logger.error("Failed to extract zip file: ", e);
        }
    }
    public static ZipFile zip(File file)
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(System.getProperty("java.io.tmpdir") + File.separator + FilenameUtils.removeExtension(file.getName()) + ".zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFile(file, parameters);
        }
        catch (ZipException e)
        {
            logger.error("ZipException: ", e);
        }
        return zipFile;
    }

    public static boolean isZipFile(String fileName) {
        return fileName.endsWith(".zip");
    }
}
