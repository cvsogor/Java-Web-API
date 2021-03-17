package com.aditor.bi.commercials.domain;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivan on 2/3/16.
 */
public class TargetConditionComparator {
    public boolean haveSameBase(TargetCondition first, TargetCondition second) {
        return first.getFieldName().equals(second.getFieldName());
    }

    public boolean isEqual(TargetCondition first, TargetCondition second) {
        if(!haveSameBase(first, second))
            return false;

        return !isBranch(first, second);
    }

    public boolean isBranch(TargetCondition first, TargetCondition second) {
        if (!haveSameBase(first, second))
            return false;

        List<String> firstValues = extractValues(first);
        List<String> secondValues = extractValues(second);

        boolean allExclusiveAreNegated = true;

//        if(first.getInverse()) {
//            firstValues = firstValues.stream().map(s -> s.substring(1)).collect(Collectors.toList());
//        }
//        if(second.getInverse()) {
//            secondValues = secondValues.stream().map(s -> s.substring(1)).collect(Collectors.toList());
//        }

        return  !shareAny(firstValues, secondValues) && allExclusiveAreNegated;
    }

    public boolean shareAny(List<String> first, List<String> second) {
        return !Sets.intersection(Sets.newHashSet(first), Sets.newHashSet(second)).isEmpty();
    }

    public List<String> removeExclusive(List<String> first, List<String> second) {

        return new ArrayList<>();
    }

    public List<String> extractValues(TargetCondition condition) {
        if(condition.getOperator() == Operator.CONTAINS)
            throw new NotImplementedException("Contains operator is not supported in this version.");

        List<String> values;
        if(condition.getOperator() == Operator.IN_LIST) {
            values = Arrays.asList(condition.getFieldValue().split(","));
        } else {
            values = Collections.singletonList(condition.getFieldValue());
        }

        return condition.getInverse() ? negateValues(values) : values;
    }

    public List<String> negateValues(List<String> values) {
        return values.stream()
                .map(s -> "!" + s)
                .collect(Collectors.toList());
    }
}
