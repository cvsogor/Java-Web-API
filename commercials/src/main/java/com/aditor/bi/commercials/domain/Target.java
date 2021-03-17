package com.aditor.bi.commercials.domain;

import com.aditor.bi.commercials.domain.exception.ConditionAlreadyExistsException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "targets")
public class Target {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @JsonIgnore
    @OneToOne(targetEntity = Commercial.class)
    private Commercial commercial;

    @Column
    private boolean isActive = false;

    @OneToMany(targetEntity = TargetCondition.class, mappedBy = "target", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<TargetCondition> conditions = new LinkedList<>();

    public Target() {
    }

    public Target(Commercial commercial) {
        this.commercial = commercial;
    }

    public List<TargetCondition> getConditions() {
        return conditions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commercial getCommercial() {
        return commercial;
    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
    }

    public Target addCondition(TargetCondition condition)
    {
        if(!conditions.stream()
                .map(TargetCondition::getFieldName)
                .collect(Collectors.toList())
                .contains(condition.getFieldName())) {
            conditions.add(condition);
        } else {
            throw new ConditionAlreadyExistsException("Condition for FieldName: " + condition.getFieldName() +
                    " already exists");
        }
        return this;
    }

    public Target removeCondition(TargetCondition condition)
    {
        conditions.remove(condition);

        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
