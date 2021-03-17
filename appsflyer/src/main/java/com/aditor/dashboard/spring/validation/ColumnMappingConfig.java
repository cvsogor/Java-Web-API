package com.aditor.dashboard.spring.validation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ColumnMappingConfig {
    private String name;
    private ColumnType type;
    private Boolean isMandatory;
    private List<String> aliases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public boolean isEqualByAlias(String alias) {
        String temp = alias.trim().toLowerCase();
        return aliases.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .anyMatch(s -> s.equals(temp));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ColumnMappingConfig that = (ColumnMappingConfig) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(type, that.type)
                .append(isMandatory, that.isMandatory)
                .append(aliases, that.aliases)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(type)
                .append(isMandatory)
                .append(aliases)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("type", type)
                .append("isMandatory", isMandatory)
                .append("aliases", aliases)
                .toString();
    }
}
