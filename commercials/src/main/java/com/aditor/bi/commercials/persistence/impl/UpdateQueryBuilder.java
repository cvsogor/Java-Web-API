package com.aditor.bi.commercials.persistence.impl;

import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.util.DateUtils;

import java.util.Date;

public class UpdateQueryBuilder extends BaseQueryBuilder {
    private static final String UPDATE_TEMPLATE = "UPDATE appsflyer_events AS <prefix> " +
            "SET <prefix>.real_revenue = :real_revenue, <prefix>.real_cost = :real_cost WHERE " +
            BASE_WHERE_TEMPLATE;

    private static final String UPDATE_DAILY_TEMPLATE = UPDATE_TEMPLATE +
            " AND <prefix>.id > :id";

    private static final String UPDATE_DATE_TEMPLATE = UPDATE_TEMPLATE +
            " AND DATE(<prefix>.event_time) = ':event_date'";

    private Double realRevenue;
    private Double realCost;
    private Date eventDate;
    private Long startingId;

    @Override
    public UpdateQueryBuilder withCommercial(Commercial commercial, String prefix) {
        super.withCommercial(commercial, prefix);
        return this;
    }

    public UpdateQueryBuilder withEventDate(Date eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public UpdateQueryBuilder withRevenue(Double revenue) {
        this.realRevenue = revenue;
        return this;
    }

    public UpdateQueryBuilder withPayout(Double payout) {
        this.realCost = payout;
        return this;
    }

    public UpdateQueryBuilder withStartingId(Long startingId) {
        this.startingId = startingId;
        return this;
    }

    public String buildGlobalUpdate() {
        return preBuild(UPDATE_TEMPLATE)
                .replace(":real_cost", this.realCost == null ? "NULL" : this.realCost.toString())
                .replace(":real_revenue", this.realRevenue == null ? "NULL" : this.realRevenue.toString());
    }

    public String buildDateUpdate() {
        return preBuild(UPDATE_DATE_TEMPLATE)
                .replace(":real_cost", this.realCost == null ? "NULL" : this.realCost.toString())
                .replace(":real_revenue", this.realRevenue == null ? "NULL" : this.realRevenue.toString())
                .replace(":event_date", DateUtils.formatDate(eventDate));
    }

    public String buildDailyUpdate() {
        String query = preBuild(UPDATE_DAILY_TEMPLATE)
                .replace(":real_cost", this.realCost == null ? "NULL" : this.realCost.toString())
                .replace(":real_revenue", this.realRevenue == null ? "NULL" : this.realRevenue.toString());

        if(startingId != null) {
            return query.replace(":id", this.startingId.toString());
        } else {
            return query.replace(":id", this.commercial.getLastUpdatedRecordId().toString());
        }
    }
}
