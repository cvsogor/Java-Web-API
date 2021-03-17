package com.aditor.bi.commercials.domain;

public enum EventType {
    INSTALL,
    FIRST_DEPOSIT,
    RETENTION_DEPOSIT,
    SIGN_UP;

    @Override
    public String toString() {
        switch (this) {
            case FIRST_DEPOSIT:
                return "ftd";
            case INSTALL:
                return "install";
            case SIGN_UP:
                return "sign_up";
            case RETENTION_DEPOSIT:
                return "deposit";
            default:
                return "";
        }
    }

    public String toAppsflyerString() {
        return this.toString();
    }
}
