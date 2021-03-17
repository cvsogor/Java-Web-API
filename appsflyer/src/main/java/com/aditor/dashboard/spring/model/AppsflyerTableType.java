package com.aditor.dashboard.spring.model;

/**
 * Created by art on 12/2/15.
 */
public enum AppsflyerTableType {

    GENERAL("appsflyer_events"),
    FULL("appsflyer_events_full");

    private String tableName;

    AppsflyerTableType(String tableName){
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
