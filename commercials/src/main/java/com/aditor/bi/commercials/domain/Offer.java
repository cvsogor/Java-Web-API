package com.aditor.bi.commercials.domain;


public class Offer {
    private String offerId;
    private CampaignModel campaignModel;
    private Double commissionPercentage;

    public Offer(String offerId, CampaignModel campaignModel, Double commissionPercentage) {
        this.offerId = offerId;
        this.campaignModel = campaignModel;
        this.commissionPercentage = commissionPercentage;
    }

    public String getOfferId() {
        return offerId;
    }

    public CampaignModel getCampaignModel() {
        return campaignModel;
    }

    public Double getCommissionPercentage() {
        return commissionPercentage;
    }
}
