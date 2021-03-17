package com.aditor.bi.commercials.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "commercials")
public class Commercial {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String offerId;

    @Column
    private String mediaSource;

    @OneToOne(targetEntity = Target.class, mappedBy = "commercial", cascade = CascadeType.ALL, orphanRemoval = true)
    private Target target;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column
    private Date dateStart;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column
    private Date dateEnd;

    @Column
    private Double amount;

    @Enumerated(EnumType.STRING)
    private CommercialType type;

    @Column
    private Long lastUpdatedRecordId = 0L;

    @Column
    private Date updatedOn = new Date();

    @PrePersist
    private void prePersist() {
        this.updatedOn = new Date();
    }

    public Commercial() {
    }

    public Commercial(String offerId,
                      Target target,
                      EventType eventType,
                      Date dateStart,
                      Date dateEnd,
                      Double amount) {
        this.offerId = offerId;
        this.target = target;
        this.eventType = eventType;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.amount = amount;
    }

    public Commercial(Commercial commercial) {
        this.offerId = commercial.getOfferId();
        this.mediaSource = commercial.getMediaSource();
        this.eventType = commercial.getEventType();
        this.type = commercial.getType();
        this.amount = commercial.getAmount();
        this.target = new Target();
        this.target.setCommercial(this);
        this.target.setActive(commercial.getTarget().isActive());
        this.dateStart = new Date();
        this.dateEnd = new Date(commercial.getDateEnd().getTime());

        for(TargetCondition condition : commercial.getTarget().getConditions()) {
            this.target.getConditions().add(new TargetCondition(condition.getOperator(), condition.getFieldName(), condition.getFieldValue(), condition.getInverse(), this.target));
        }
    }

    public Long getId() {
        return id;
    }

    public String getOfferId() {
        return offerId;
    }

    public Target getTarget() {
        return target;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Double getAmount() {
        return amount;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public CommercialType getType() {
        return type;
    }

    public Long getLastUpdatedRecordId() {
        return lastUpdatedRecordId;
    }

    public boolean isActive() {
        return target != null && target.isActive();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }

    public void setType(CommercialType type) {
        this.type = type;
    }

    public void setLastUpdatedRecordId(Long lastUpdatedRecordId) {
        this.lastUpdatedRecordId = lastUpdatedRecordId;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public static class Builder {
        private Commercial commercial;
        public Builder() {
            commercial = new Commercial();
        }

        public Builder withOfferId(String offerId) {
            commercial.setOfferId(offerId);
            return this;
        }

        public Builder withMediaSource(String mediaSource) {
            commercial.setMediaSource(mediaSource);
            return this;
        }

        public Builder withActiveTarget() {
            commercial.setTarget(new Target(commercial));
            commercial.getTarget().setActive(true);
            return this;
        }

        public Builder withTarget(boolean isActive) {
            commercial.setTarget(new Target(commercial));
            commercial.getTarget().setActive(isActive);
            return this;
        }

        public Builder withStartDate(Date date) {
            commercial.setDateStart(date);
            return this;
        }

        public Builder withEndDate(Date date) {
            commercial.setDateEnd(date);
            return this;
        }

        public Builder withType(CommercialType type) {
            commercial.setType(type);
            return this;
        }

        public Builder withEventType(EventType type) {
            commercial.setEventType(type);
            return this;
        }

        public Builder withAmount(Double amount) {
            commercial.setAmount(amount);
            return this;
        }

        public Builder withCondition(Operator operator, String fieldName, String fieldValue, Boolean isInverse) {
            commercial.getTarget().addCondition(new TargetCondition(operator,
                    fieldName,
                    fieldValue,
                    isInverse,
                    commercial.getTarget()));
            return this;
        }

        public Commercial build() {
            return commercial;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Commercial that = (Commercial) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(offerId, that.offerId)
                .append(mediaSource, that.mediaSource)
                .append(eventType, that.eventType)
                .append(dateStart, that.dateStart)
                .append(dateEnd, that.dateEnd)
                .append(type, that.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(offerId)
                .append(mediaSource)
                .append(eventType)
                .append(dateStart)
                .append(dateEnd)
                .append(type)
                .toHashCode();
    }
}
