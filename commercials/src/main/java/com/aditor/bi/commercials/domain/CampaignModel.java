package com.aditor.bi.commercials.domain;


import com.google.common.base.CaseFormat;

public enum CampaignModel {
    NETWORK,
    AGENCY;

    @Override
    public String toString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
    }
}
