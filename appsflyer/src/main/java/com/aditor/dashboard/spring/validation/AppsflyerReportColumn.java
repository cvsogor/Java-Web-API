package com.aditor.dashboard.spring.validation;


import java.util.ArrayList;
import java.util.List;

public class AppsflyerReportColumn {
    private Integer index;
    private String headerName;
    private List<String> sampledValues = new ArrayList<>();
    private ColumnMappingConfig columnMappingConfig;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public List<String> getSampledValues() {
        return sampledValues;
    }

    public void setSampledValues(List<String> sampledValues) {
        this.sampledValues = sampledValues;
    }

    public ColumnMappingConfig getColumnMappingConfig() {
        return columnMappingConfig;
    }

    public void setColumnMappingConfig(ColumnMappingConfig columnMappingConfig) {
        this.columnMappingConfig = columnMappingConfig;
    }

    public void addValue(String value) {
        sampledValues.add(value);
    }

    public boolean isMatched() {
        return this.columnMappingConfig != null;
    }
}
