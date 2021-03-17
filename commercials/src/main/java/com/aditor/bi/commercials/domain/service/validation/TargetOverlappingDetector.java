package com.aditor.bi.commercials.domain.service.validation;


import com.aditor.bi.commercials.domain.Target;
import com.aditor.bi.commercials.domain.TargetCondition;
import com.aditor.bi.commercials.domain.util.SetUtils;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TargetOverlappingDetector {
    private TargetConditionOverlappingDetector conditionCollisionDetector = new TargetConditionOverlappingDetector();

    public boolean areConflicting(Target left, Target right) {
        Map<String, TargetCondition> leftFieldValueMap = left.getConditions().stream()
                .collect(Collectors.toMap(TargetCondition::getFieldName, Function.identity()));
        Map<String, TargetCondition> rightFieldValueMap = right.getConditions().stream()
                .collect(Collectors.toMap(TargetCondition::getFieldName, Function.identity()));

        Set<String> leftFieldNames = leftFieldValueMap.keySet();
        Set<String> rightFieldNames = rightFieldValueMap.keySet();
        Set<String> intersection = Sets.intersection(leftFieldNames, rightFieldNames);

        boolean intersectionIsBranching = intersection.stream().map(fieldName -> {
            return conditionCollisionDetector.fieldValuesAreBranching(leftFieldValueMap.get(fieldName), rightFieldValueMap.get(fieldName));
        }).anyMatch(atLeastOneFieldIsBranching -> atLeastOneFieldIsBranching);

        if(SetUtils.isDisjoint(leftFieldNames, rightFieldNames)) {
            return true;
        }

        if(!intersectionIsBranching) {
            return !SetUtils.isSuperSet(leftFieldNames, rightFieldNames) && !SetUtils.isSuperSet(rightFieldNames, leftFieldNames);
        }

        return false;
    }
}
