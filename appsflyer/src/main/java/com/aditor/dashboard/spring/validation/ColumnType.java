package com.aditor.dashboard.spring.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ColumnType {
    // TODO: add jackson type serializer for this type.
    @JsonProperty("string")
    STRING("string"),
    @JsonProperty("datetime")
    DATETIME("datetime"),
    @JsonProperty("double")
    DOUBLE("double");

    private final String type;

    private ColumnType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}
