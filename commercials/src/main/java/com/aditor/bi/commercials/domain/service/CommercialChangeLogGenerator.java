package com.aditor.bi.commercials.domain.service;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialLogEntry;
import com.aditor.bi.commercials.domain.Target;
import com.aditor.bi.commercials.domain.TargetCondition;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommercialChangeLogGenerator {
    public List<CommercialLogEntry> generateLog(Commercial oldCommercial, Commercial newCommercial) {
        return generateCommercialChangeList(oldCommercial, newCommercial).stream()
                .map(commercialLogEntry -> {
                    commercialLogEntry.setCommercialId(oldCommercial.getId());
                    commercialLogEntry.setCreatedAt(new Date());
                    commercialLogEntry.setCreatedBy("user");

                    return commercialLogEntry;
                })
                .collect(Collectors.toList());
    }

    private List<CommercialLogEntry> generateCommercialChangeList(Commercial left, Commercial right) {
        List<CommercialLogEntry> changeList = new ArrayList<>();

        if(!Objects.equals(left.getOfferId(), right.getOfferId())) {
            changeList.add(generateLogEntry("offerId", left.getOfferId(), right.getOfferId()));
        }

        if(!Objects.equals(left.getMediaSource(), right.getMediaSource())) {
            changeList.add(generateLogEntry("mediaSource", left.getMediaSource(), right.getMediaSource()));
        }

        if(!left.getEventType().equals(right.getEventType())) {
            changeList.add(generateLogEntry("eventType", left.getEventType(), right.getEventType()));
        }

        if(!left.getDateStart().equals(right.getDateStart())) {
            changeList.add(generateLogEntry("dateStart", left.getDateStart(), right.getDateStart()));
        }

        if(!left.getDateEnd().equals(right.getDateEnd())) {
            changeList.add(generateLogEntry("dateStart", left.getDateEnd(), right.getDateEnd()));
        }

        if(!left.getType().equals(right.getType())) {
            changeList.add(generateLogEntry("type", left.getType(), right.getType()));
        }

        if(!Objects.equals(left.getAmount(), right.getAmount())) {
            changeList.add(generateLogEntry("amount", left.getAmount(), right.getAmount()));
        }

        changeList.addAll(generateTargetChangeList(left.getTarget(), right.getTarget()));
        return changeList;
    }

    private List<CommercialLogEntry> generateTargetChangeList(Target left, Target right) {
        List<CommercialLogEntry> changeList = new ArrayList<>();

        if(left.isActive() != right.isActive()) {
            changeList.add(generateLogEntry("isActive", left.isActive(), right.isActive()));
        }

        Map<String, TargetCondition> leftConditions = new HashMap<>();
        Map<String, TargetCondition> rightConditions = new HashMap<>();
        Set<String> fieldNames = new HashSet<>();

        left.getConditions().forEach(targetCondition -> {
            leftConditions.put(targetCondition.getFieldName(), targetCondition);
            fieldNames.add(targetCondition.getFieldName());
        });
        right.getConditions().forEach(targetCondition -> {
            rightConditions.put(targetCondition.getFieldName(), targetCondition);
            fieldNames.add(targetCondition.getFieldName());
        });

        fieldNames.forEach(fieldName -> {
            changeList.addAll(generateTargetConditionChangeList(leftConditions.get(fieldName), rightConditions.get(fieldName)));
        });

        return changeList;
    }

    private List<CommercialLogEntry> generateTargetConditionChangeList(TargetCondition left, TargetCondition right) {
        List<CommercialLogEntry> changeList = new ArrayList<>();

        if(left != right && (left == null || right == null)) {
            if(left == null) {
                changeList.add(generateLogEntry(right.getFieldName(), "null", right.toString()));
            } else {
                changeList.add(generateLogEntry(left.getFieldName(), left.toString(), "null"));
            }
        } else {
            if(!left.equals(right)) {
                changeList.add(generateLogEntry(left.getFieldName(), left.toString(), right.toString()));
            }
        }

        return changeList;
    }

    private <T> CommercialLogEntry generateLogEntry(String fieldName, T oldValue, T newValue) {
        String oldValueString = oldValue == null ? "null" : String.valueOf(oldValue);
        String newValueString = newValue == null ? "null" : String.valueOf(newValue);

        return new CommercialLogEntry(fieldName, oldValueString, newValueString);
    }

}
