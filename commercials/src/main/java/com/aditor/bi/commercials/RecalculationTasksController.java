package com.aditor.bi.commercials;

import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.RecalculationStatus;
import com.aditor.bi.commercials.domain.RecalculationTask;
import com.aditor.bi.commercials.domain.User;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.persistence.RecalculationTaskRepository;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import com.aditor.bi.commercials.tracking.CommercialCostsCalculator;
import com.aditor.bi.commercials.tracking.CommercialUpdateNotificationService;
import com.aditor.bi.commercials.tracking.TrackingRecordUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequestMapping("/api/commercials")
@RestController
public class RecalculationTasksController {

    @Autowired
    CommercialRepository commercialRepository;

    @Autowired
    TrackingRecordRepository trackingRecordRepository;

    @Autowired
    CommercialCostsCalculator costsCalculator;

    @Autowired
    TrackingRecordUpdater updater;

    @Autowired
    RecalculationTaskRepository recalculationTaskRepository;

    @Autowired
    CommercialUpdateNotificationService updateNotificationService;

    private AtomicBoolean globalUpdateRunning = new AtomicBoolean(false);

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/global-recalculation-task", method = RequestMethod.GET)
    public List<RecalculationTask> getAllActiveUpdateTasks() {
        return recalculationTaskRepository.findAllInProgress();
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/global-recalculation-task", method = RequestMethod.POST)
    public List<RecalculationTask> startGlobalRecalculation() {

        if(globalUpdateRunning.compareAndSet(false, true)) {
            new Thread(() -> {
                commercialRepository.findAllActive().forEach(commercial -> {
                    try {
                        postUpdateTask(commercial.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    globalUpdateRunning.set(false);
                });
            }).start();
        }

        return recalculationTaskRepository.findAllInProgress();
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/{commercialId}/recalculation-tasks", method = RequestMethod.POST)
    public RecalculationTask postUpdateTask(@PathVariable Long commercialId) throws Exception {
        Commercial commercial = commercialRepository.findOne(commercialId);

        List<RecalculationTask> activeRecalculations = recalculationTaskRepository
                .findAllByStatusAndCommercial(commercial, RecalculationStatus.IN_PROGRESS);
        RecalculationTask recalculationTask;


        if(activeRecalculations.isEmpty()) {
            recalculationTask = new RecalculationTask("", "", commercial);
        } else {
            return activeRecalculations.get(0);
        }

        return updater.startRecalculation(recalculationTask);
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/{commercialId}/recalculation-tasks", method = RequestMethod.GET)
    public List<RecalculationTask> getUpdateTask(@PathVariable Long commercialId) throws Exception {
        Commercial commercial = commercialRepository.findOne(commercialId);
        return recalculationTaskRepository.findAllByCommercial(commercial);
    }
}
