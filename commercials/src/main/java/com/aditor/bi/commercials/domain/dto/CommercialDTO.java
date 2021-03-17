package com.aditor.bi.commercials.domain.dto;

import com.aditor.bi.commercials.domain.CommercialType;
import com.aditor.bi.commercials.domain.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CommercialDTO {
    public Long id;

    public String offerId;

    public String mediaSource;

    public Date dateStart;

    public Date dateEnd;

    public EventType eventType;

    public CommercialType type;

    public boolean isActive;

    public Double amount;

    public Set<TargetConditionDTO> conditions = new HashSet<>();

    public Date updatedOn;

    public CommercialDTO() {
    }

    public CommercialDTO(String offerId, String mediaSource, Date dateStart, Date dateEnd, EventType eventType, CommercialType type, boolean isActive, Double amount) {
        this.offerId = offerId;
        this.mediaSource = mediaSource;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.eventType = eventType;
        this.type = type;
        this.isActive = isActive;
        this.amount = amount;
    }
}
