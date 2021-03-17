package com.aditor.bi.commercials.domain.dto;

import com.aditor.bi.commercials.domain.Operator;

public class TargetConditionDTO {
    public Operator operator;
    public String fieldName;
    public String fieldValue;
    public Boolean isInverse;

    public TargetConditionDTO() {
    }

    public TargetConditionDTO(Operator operator, String fieldName, String fieldValue, Boolean isInverse) {
        this.operator = operator;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.isInverse = isInverse;
    }
}
