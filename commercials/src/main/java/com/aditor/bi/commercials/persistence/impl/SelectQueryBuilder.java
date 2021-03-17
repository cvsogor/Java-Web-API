package com.aditor.bi.commercials.persistence.impl;


import com.aditor.bi.commercials.domain.Commercial;

public class SelectQueryBuilder extends BaseQueryBuilder {
    private static final String SELECT_DATE_RANGE_TEMPLATE = "SELECT DISTINCT DATE(<prefix>.event_time) " +
            "FROM appsflyer_events AS <prefix> WHERE " +
            BASE_WHERE_TEMPLATE;

    private static final String SELECT_MAX_ID = "SELECT MAX(<prefix>.id) " +
            "FROM appsflyer_events AS <prefix> WHERE " +
            "<prefix>.id > :last_updated_id AND " +
            BASE_WHERE_TEMPLATE;

    private static final String SELECT_DAILY_UPDATE_TEMPLATE = "" +
            "<prefix>.id > :last_updated_id AND " +
            BASE_WHERE_TEMPLATE;

    private static final String SELECT_SAMPLE_TEMPLATE = "SELECT <prefix>.* " +
            "FROM appsflyer_events AS <prefix> WHERE " +
            BASE_WHERE_TEMPLATE +
            " LIMIT 100";

    @Override
    public SelectQueryBuilder withCommercial(Commercial commercial, String prefix) {
        super.withCommercial(commercial, prefix);
        return this;
    }

    public String buildDateRange() {
        return preBuild(SELECT_DATE_RANGE_TEMPLATE);
    }

    public String buildDailyUpdate() {
        return preBuild(SELECT_DAILY_UPDATE_TEMPLATE)
                .replace(":last_updated_id", commercial.getLastUpdatedRecordId().toString());
    }

    public String buildSelectMaxRecordId() {
        return preBuild(SELECT_MAX_ID)
                .replace(":last_updated_id", commercial.getLastUpdatedRecordId().toString());
    }

    public String buildSelectSample() {
        return preBuild(SELECT_SAMPLE_TEMPLATE);
    }

    public String buildWherePart() {
        return preBuild(BASE_WHERE_TEMPLATE);
    }
}
