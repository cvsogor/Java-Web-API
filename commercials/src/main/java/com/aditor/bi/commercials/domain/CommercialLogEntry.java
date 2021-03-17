package com.aditor.bi.commercials.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commercial_changelog")
public class CommercialLogEntry {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private Long commercialId;

    @Column
    private Date createdAt = new Date();

    @Column
    private String createdBy;

    @Column
    private String field;

    @Column
    private String oldValue;

    @Column
    private String newValue;

    public CommercialLogEntry() {
    }

    public CommercialLogEntry(String field, String oldValue, String newValue) {
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
