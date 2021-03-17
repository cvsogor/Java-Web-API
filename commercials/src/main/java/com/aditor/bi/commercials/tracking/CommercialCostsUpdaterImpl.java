package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialCosts;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(BuildProfiles.PROD)
@Service
public class CommercialCostsUpdaterImpl implements CommercialCostsUpdater {
    private CommercialCostsCalculator costsCalculator;
    private TrackingRecordRepository trackingRecordRepository;
    private final Logger logger = LoggerFactory.getLogger(CommercialCostsUpdaterImpl.class);

    @Autowired
    public CommercialCostsUpdaterImpl(CommercialCostsCalculator costsCalculator, TrackingRecordRepository trackingRecordRepository) {
        this.costsCalculator = costsCalculator;
        this.trackingRecordRepository = trackingRecordRepository;
    }

    @Override
    public void update(Commercial commercial) {
        CommercialCosts costs = costsCalculator.calculate(commercial);
        int affected = trackingRecordRepository.updateCosts(commercial, costs.getRevenue(), costs.getPayout());
        logger.info("Commercial #" + commercial.getId() + ": updated " + affected + " records");
    }

    @Override
    public void reset(Commercial commercial) {
        int affected = trackingRecordRepository.resetCosts(commercial);
        logger.info("Commercial #" + commercial.getId() + " : cleared " + affected + " records");
    }
}
