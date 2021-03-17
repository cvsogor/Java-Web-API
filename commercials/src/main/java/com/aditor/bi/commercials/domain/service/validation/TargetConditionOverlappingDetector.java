package com.aditor.bi.commercials.domain.service.validation;

import com.aditor.bi.commercials.domain.TargetCondition;
import com.aditor.bi.commercials.domain.util.SetUtils;
import com.google.common.collect.Sets;

import java.util.Set;

public class TargetConditionOverlappingDetector {
    private static final String LIST_SEPARATOR = ",";

    public boolean fieldValuesAreBranching(TargetCondition left, TargetCondition right) {
        Set<String> leftFieldValues = getValueSet(left);
        Set<String> rightFieldValues = getValueSet(right);

        if(left.getInverse() == right.getInverse()) {
            return SetUtils.isDisjoint(leftFieldValues, rightFieldValues);
        } else if(left.getInverse()) {
            return SetUtils.isSuperSet(leftFieldValues, rightFieldValues) ||
                    SetUtils.isEqual(leftFieldValues, rightFieldValues);
        } else if(right.getInverse()) {
            return SetUtils.isSuperSet(rightFieldValues, leftFieldValues) ||
                    SetUtils.isEqual(rightFieldValues, leftFieldValues);
        }

        return false;
    }

    public Set<String> getValueSet(TargetCondition condition) {
        switch (condition.getOperator()) {
            case IN_LIST:
                return Sets.newHashSet(condition.getFieldValue().split(LIST_SEPARATOR));
            case EQUALS:
                return Sets.newHashSet(condition.getFieldValue());
            default:
                throw new RuntimeException("Not implemented yet");
        }
    }

}
