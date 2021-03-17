package com.aditor.bi.commercials.domain;

public class CommercialCosts {
    private Double revenue;
    private Double payout;

    public CommercialCosts(Double revenue, Double payout) {
        this.revenue = revenue;
        this.payout = payout;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Double getPayout() {
        return payout;
    }
}
