package com.aditor.bi.commercials.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "target_conditions")
public class TargetCondition {

    private static final String LIST_SEPARATOR = ",";

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String fieldName = "I'm undefined";

    @Column
    private String fieldValue = "I'm also undefined";

    @Column
    private Boolean isInverse = false;

    @Enumerated(EnumType.STRING)
    private Operator operator;

    @JsonIgnore
    @ManyToOne(targetEntity = Target.class)
    private Target target;

    public TargetCondition() {}

    public TargetCondition(Operator operator, String fieldName, String fieldValue, Boolean isInverse, Target target)
    {
        this.operator = operator;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.isInverse = isInverse;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public Boolean getInverse() {
        return isInverse;
    }

    public Target getTarget() {
        return target;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void setInverse(Boolean inverse) {
        isInverse = inverse;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public boolean isSemanticallyEqualTo(TargetCondition targetCondition)
    {
        TargetConditionComparator comparator = new TargetConditionComparator();
        return comparator.isEqual(this, targetCondition) || comparator.isEqual(targetCondition, this);
    }

    public boolean matchesValue(Object object)
    {
        if(object == null)
            return false;

        String value = (String) object;

        if(operator == Operator.EQUALS) {
            if(value.equals(this.fieldValue))
                return true;
        } else if(operator == Operator.IN_LIST) {
            return Arrays.asList(fieldValue.split(LIST_SEPARATOR)).stream()
                    .anyMatch(s -> s.equals(value));
        }

        return false;
    }

    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;

        if(!(object instanceof TargetCondition))
            return false;

        TargetCondition condition = (TargetCondition) object;

        return this.isSemanticallyEqualTo(condition);
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(id)
                .append(operator)
                .append(fieldName)
                .append(fieldValue);

        return builder.build();
    }

    @Override
    public String toString() {
        return "{" +
                "operator: " + operator +
                ", isInverse:" + isInverse +
                ", fieldValue:'" + fieldValue + '\'' +
                ", fieldName:'" + fieldName + '\'' +
                '}';
    }
}
