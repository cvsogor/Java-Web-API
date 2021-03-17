package com.aditor.dashboard.spring.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColumnMappingConfigsHolder {

    Map<String, List<ColumnMappingConfig>> configList = new HashMap<>();

    public ColumnMappingConfig getByFieldName(String fieldName, String type) {
        for(ColumnMappingConfig config : configList.get(type)) {
            if(config.getName().equals(fieldName))
                return config;
        }

        return null;
    }

    public ColumnMappingConfig getByAlias(String alias, String type) {
        for(ColumnMappingConfig config : configList.get(type)) {
            if(config.isEqualByAlias(alias))
                return config;
        }

        return null;
    }

    public ColumnMappingConfig getByIndex(int index, String type) {
        try {
            return configList.get(type).get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public List<String> getAllMandatoryColumnNames(String type) {
        return configList.get(type).stream()
                .filter(ColumnMappingConfig::isMandatory)
                .map(ColumnMappingConfig::getName)
                .collect(Collectors.toList());
    }

    public List<ColumnMappingConfig> getConfigList(String type) {
        return configList.get(type);
    }

    public void addConfigList(List<ColumnMappingConfig> configList, String type) {
        this.configList.put(type, configList);
    }
}
