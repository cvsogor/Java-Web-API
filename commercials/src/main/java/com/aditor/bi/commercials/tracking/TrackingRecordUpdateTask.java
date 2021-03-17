package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.domain.CommercialCosts;
import com.aditor.bi.commercials.domain.RecalculationTask;
import com.aditor.bi.commercials.persistence.RecalculationTaskRepository;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

class TrackingRecordUpdateTask implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(TrackingRecordUpdateTask.class);
    private RecalculationTaskRepository recalculationTaskRepository;
    private RecalculationTask recalculationTask;
    private TrackingRecordRepository trackingRecordRepository;
    private CommercialCostsCalculator costsCalculator;
    private CommercialUpdateNotificationService updateNotificationService;

    public TrackingRecordUpdateTask(RecalculationTaskRepository recalculationTaskRepository,
                                    RecalculationTask recalculationTask,
                                    TrackingRecordRepository trackingRecordRepository,
                                    CommercialCostsCalculator costsCalculator,
                                    CommercialUpdateNotificationService updateNotificationService) {
        this.recalculationTaskRepository = recalculationTaskRepository;
        this.recalculationTask = recalculationTask;
        this.trackingRecordRepository = trackingRecordRepository;
        this.costsCalculator = costsCalculator;
        this.updateNotificationService = updateNotificationService;
    }

    @Override
    public Integer call() throws Exception {
        CommercialCosts costs = costsCalculator.calculate(recalculationTask.getCommercial());
        logger.info("Calculated costs for commercial" + recalculationTask.getCommercial().getId() + " , payout: " + costs.getPayout() +
                " revenue: " + costs.getRevenue());

        int affectedRows = trackingRecordRepository.updateCosts(recalculationTask.getCommercial(), costs.getRevenue(), costs.getPayout());

        logger.info("Updated " + affectedRows);

        logger.info("Notifying on commercial update");
        updateNotificationService.notify(recalculationTask.getCommercial());

        recalculationTask.finish();
        recalculationTaskRepository.saveAndFlush(recalculationTask);
        return affectedRows;
    }
}
