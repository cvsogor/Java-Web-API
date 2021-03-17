package com.aditor.bi.commercials.domain.model;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialType;

public class NetworkCampaignModelCalculationStrategy implements CampaignModelCalculationStrategy {
    @Override
    public Double calculateRevenue(Commercial commercial) {
        if(commercial.getType() == CommercialType.REVENUE) {
            return commercial.getAmount();
        } else {
            return null;
        }
    }

    @Override
    public Double calculatePayout(Commercial commercial) {
        if(commercial.getType() == CommercialType.PAYOUT) {
            return commercial.getAmount();
        } else {
            return null;
        }
    }
}
