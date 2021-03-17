package com.aditor.dashboard.spring.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ColumnMappingConfigsLoader {
    private static final String MAPPING_CONFIG_DIR = "mapping/";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ColumnMappingConfigsLoader.class);

    public Map<String, List<ColumnMappingConfig>> getColumnConfigs() {

        Map<String, List<ColumnMappingConfig>> configs = new HashMap<>();

        Optional<List<File>> fileList = getConfigDir(MAPPING_CONFIG_DIR)
                .flatMap(this::getFileList);

        if(fileList.isPresent()) {
            for(File file : fileList.get()) {
                List<ColumnMappingConfig> columnMappingConfig = getConfigFileContents(file)
                        .flatMap(this::parseConfigs)
                        .orElse(new ArrayList<>());
                configs.put(getConfigName(file.getName()), columnMappingConfig);
            }
        }

        return configs;
    }

    protected Optional<File> getConfigDir(String configDir) {
        try {
            File file = new ClassPathResource(configDir).getFile();
            return Optional.of(file);
        } catch (IOException exception) {
            logger.error("Failed to find config dir: ", exception);
            return Optional.empty();
        }
    }

    protected Optional<List<File>> getFileList(File directory) {

        Optional<List<File>> fileList = Optional.of(Arrays.asList(directory.listFiles()));

        if(fileList.isPresent() && fileList.get().size() > 0) {
            return fileList;
        } else {
            logger.error("Config directory is empty");
            return Optional.empty();
        }
    }

    protected Optional<File> getConfigFile(String fileName) {
        try {
            File file = new ClassPathResource(fileName).getFile();
            return Optional.of(file);
        } catch (IOException exception) {
            logger.error("Failed to get configs file: ", exception);
            return Optional.empty();
        }
    }

    protected Optional<String> getConfigFileContents(File file) {
        try {
            String contents = FileUtils.readFileToString(file);
            return Optional.of(contents);
        } catch (IOException exception) {
            logger.error("Failed to read configs file: ", exception);
            return Optional.empty();
        }
    }

    protected Optional<List<ColumnMappingConfig>> parseConfigs(String serializedConfigs) {
        try {
            List<ColumnMappingConfig> configs = Arrays.asList(mapper.readValue(serializedConfigs, ColumnMappingConfig[].class));
            return Optional.of(configs);
        } catch (Exception exception) {
            logger.error("Failed to parse configs: ", exception);
            return Optional.empty();
        }
    }

    protected String getConfigName(String fileName) {
        return (fileName.split("\\."))[0];
    }
}
