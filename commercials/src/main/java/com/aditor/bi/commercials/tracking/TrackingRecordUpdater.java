package com.aditor.bi.commercials.tracking;


import com.aditor.bi.commercials.domain.RecalculationTask;
import com.aditor.bi.commercials.persistence.RecalculationTaskRepository;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class TrackingRecordUpdater {
    private AsyncTaskExecutor taskExecutor;
    private RecalculationTaskRepository recalculationTaskRepository;
    private CommercialCostsCalculator costsCalculator;
    private TrackingRecordRepository trackingRecordRepository;
    private CommercialUpdateNotificationService commercialUpdateNotificationService;

    @Autowired
    public TrackingRecordUpdater(RecalculationTaskRepository recalculationTaskRepository,
                                 AsyncTaskExecutor taskExecutor,
                                 CommercialCostsCalculator costsCalculator,
                                 TrackingRecordRepository trackingRecordRepository,
                                 CommercialUpdateNotificationService commercialUpdateNotificationService) {
        this.recalculationTaskRepository = recalculationTaskRepository;
        this.taskExecutor = taskExecutor;
        this.costsCalculator = costsCalculator;
        this.trackingRecordRepository = trackingRecordRepository;
        this.commercialUpdateNotificationService = commercialUpdateNotificationService;
    }

    public RecalculationTask startRecalculation(RecalculationTask recalculationTask) {
        recalculationTask.start();
        recalculationTask = recalculationTaskRepository.saveAndFlush(recalculationTask);

        taskExecutor.submit(new TrackingRecordUpdateTask(recalculationTaskRepository,
                recalculationTask,
                trackingRecordRepository,
                costsCalculator,
                commercialUpdateNotificationService));

        return recalculationTask;
    }
}

