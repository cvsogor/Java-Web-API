package com.aditor.bi.commercials.domain.model;

import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialType;

public class AgencyCampaignModelCalculationStrategy implements CampaignModelCalculationStrategy {

    private Double commissionPercentage;

    public AgencyCampaignModelCalculationStrategy(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    @Override
    public Double calculateRevenue(Commercial commercial) {
        return ((commercial.getAmount() / 100.0) * commissionPercentage) + commercial.getAmount();
    }

    @Override
    public Double calculatePayout(Commercial commercial) {
        return commercial.getAmount();
    }
}
