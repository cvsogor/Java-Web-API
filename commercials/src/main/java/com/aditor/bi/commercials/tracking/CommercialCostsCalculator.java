package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.domain.*;
import com.aditor.bi.commercials.domain.model.AgencyCampaignModelCalculationStrategy;
import com.aditor.bi.commercials.domain.model.CampaignModelCalculationStrategy;
import com.aditor.bi.commercials.domain.model.NetworkCampaignModelCalculationStrategy;
import com.aditor.bi.commercials.persistence.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommercialCostsCalculator {
    OfferRepository offerRepository;

    @Autowired
    public CommercialCostsCalculator(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public CommercialCosts calculate(Commercial commercial) {
        if(commercial.getType() == CommercialType.PAYOUT) {
            Offer offer = offerRepository.findOne(commercial.getOfferId());

            if(offer == null)
                return new CommercialCosts(null, null);

            CampaignModelCalculationStrategy strategy = offer.getCampaignModel() == CampaignModel.AGENCY ?
                    new AgencyCampaignModelCalculationStrategy(offer.getCommissionPercentage()) :
                    new NetworkCampaignModelCalculationStrategy();

            return new CommercialCosts(strategy.calculateRevenue(commercial), strategy.calculatePayout(commercial));
        } else {
            return new CommercialCosts(commercial.getAmount(), null);
        }
    }
}
