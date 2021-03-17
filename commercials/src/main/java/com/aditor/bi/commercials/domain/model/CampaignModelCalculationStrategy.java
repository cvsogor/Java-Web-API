package com.aditor.bi.commercials.domain.model;

import com.aditor.bi.commercials.domain.Commercial;

public interface CampaignModelCalculationStrategy {
    Double calculateRevenue(Commercial commercial);
    Double calculatePayout(Commercial commercial);
}
