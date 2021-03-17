package com.aditor.bi.commercials.persistence.impl;

import com.aditor.bi.commercials.domain.TargetCondition;

import java.util.Arrays;


public class ConditionSubQueryBuilder {
    private static final String EQUALS_TEMPLATE = "<field-name>) = ':value'";
    private static final String IN_LIST_TEMPLATE = "<field-name>) IN (:value)";
    private static final String CONTAINS_TEMPLATE = "<field-name>) LIKE '%:value%'";

    private String conditions = "TRUE";

    public ConditionSubQueryBuilder withCondition(TargetCondition condition) {
        return withCondition(condition, "");
    }

    public ConditionSubQueryBuilder withCondition(TargetCondition condition, String prefix) {
        String conditionStr = "";
        switch (condition.getOperator()) {
            case EQUALS:
                conditionStr = EQUALS_TEMPLATE
                        .replace("<field-name>", condition.getFieldName())
                        .replace(":value", condition.getFieldValue().toLowerCase());
                break;
            case CONTAINS:
                conditionStr = CONTAINS_TEMPLATE
                        .replace("<field-name>", condition.getFieldName())
                        .replace(":value", condition.getFieldValue().toLowerCase());
                break;
            case IN_LIST:
                conditionStr = IN_LIST_TEMPLATE;

                String valuesArray = Arrays.asList(condition.getFieldValue().split(",")).stream()
                        .filter(s -> !s.isEmpty())
                        .map(s -> "'" + s.toLowerCase() + "'")
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse("");

                if(valuesArray.isEmpty())
                    return this;

                conditionStr = conditionStr
                        .replace("<field-name>", condition.getFieldName())
                        .replace(":value", valuesArray);

                break;
        }

        if(prefix != null && !prefix.isEmpty())
            conditionStr = prefix + "." + conditionStr;
        conditionStr = "LOWER(" + conditionStr;

        if(condition.getInverse())
            conditionStr = "NOT (" + conditionStr + ")";

        conditions += " AND " + conditionStr;

        return this;
    }

    public String build() {

        return conditions;
    }
}
