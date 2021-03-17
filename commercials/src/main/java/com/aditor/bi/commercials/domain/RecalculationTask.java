package com.aditor.bi.commercials.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recalculation_tasks")
public class RecalculationTask {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Date createdOn;

    @Column
    private Date startedOn;

    @Column
    private Date finishedOn;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(targetEntity = Commercial.class)
    @JoinColumn(name = "commercial_id")
    private Commercial commercial;

    @Enumerated(EnumType.STRING)
    private RecalculationStatus status;

    public RecalculationTask() {
    }

    public RecalculationTask(String name, String description, Commercial commercial) {
        this.createdOn = new Date();
        this.name = name;
        this.description = description;
        this.commercial = commercial;
        this.status = RecalculationStatus.CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Commercial getCommercial() {
        return commercial;
    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
    }

    public RecalculationStatus getStatus() {
        return status;
    }

    public void setStatus(RecalculationStatus status) {
        this.status = status;
    }

    public void start() {
        this.status = RecalculationStatus.IN_PROGRESS;
        this.startedOn = new Date();
    }

    public void finish() {
        this.status = RecalculationStatus.FINISHED;
        this.finishedOn = new Date();
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getStartedOn() {
        return startedOn;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }
}
