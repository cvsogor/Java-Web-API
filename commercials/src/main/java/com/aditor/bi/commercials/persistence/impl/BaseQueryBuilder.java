package com.aditor.bi.commercials.persistence.impl;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.TargetCondition;
import com.aditor.bi.commercials.domain.util.DateUtils;

public abstract class BaseQueryBuilder {
    protected String prefix;
    protected ConditionSubQueryBuilder conditionSubQueryBuilder = new ConditionSubQueryBuilder();

    protected Commercial commercial;

    protected static final String BASE_WHERE_TEMPLATE = "(LOWER(<prefix>.offer_id) = ':offer_id' AND " +
            "(':media_source' = '' OR LOWER(<prefix>.media_source) = ':media_source')) " +
            "AND LOWER(<prefix>.event_name) = ':event_name' " +
            "AND DATE(<prefix>.event_time) >= ':date_start' " +
            "AND DATE(<prefix>.event_time) <= ':date_end' " +
            "AND (<conditions>)";

    public BaseQueryBuilder withCommercial(Commercial commercial, String prefix) {
        this.commercial = commercial;
        if(prefix == null || prefix.isEmpty()) {
            this.prefix = "appsflyer_events";
        } else {
            this.prefix = prefix;
        }

        for(TargetCondition condition : commercial.getTarget().getConditions()) {
            this.withCondition(condition);
        }

        return this;
    }

    public BaseQueryBuilder withCondition(TargetCondition condition) {
        conditionSubQueryBuilder.withCondition(condition, prefix);

        return this;
    }

    public String preBuild(String queryTemplate) {
        return queryTemplate.replace("<prefix>", prefix)
                .replace(":offer_id", commercial.getOfferId().toLowerCase())
                .replace(":media_source", commercial.getMediaSource() == null ? "" : commercial.getMediaSource().toLowerCase())
                .replace(":event_name", commercial.getEventType().toAppsflyerString().toLowerCase())
                .replace(":date_start", DateUtils.formatDate(commercial.getDateStart()))
                .replace(":date_end", DateUtils.formatDate(commercial.getDateEnd()))
                .replace("<conditions>", conditionSubQueryBuilder.build());
    }
}
