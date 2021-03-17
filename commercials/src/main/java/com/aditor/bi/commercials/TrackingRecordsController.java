package com.aditor.bi.commercials;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialCosts;
import com.aditor.bi.commercials.domain.RecalculationStatus;
import com.aditor.bi.commercials.domain.RecalculationTask;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.persistence.RecalculationTaskRepository;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import com.aditor.bi.commercials.tracking.CommercialCostsCalculator;
import com.aditor.bi.commercials.tracking.TrackingFieldValueRegistry;
import com.aditor.bi.commercials.tracking.TrackingRecordUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequestMapping("/api/tracking-records/")
@RestController
public class TrackingRecordsController {

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
    TrackingFieldValueRegistry fieldValueRegistry;

    @RequestMapping("/{commercialId}")
    public List<Map<String, Object>> getList(@PathVariable Long commercialId) {
        Commercial commercial = commercialRepository.findOne(commercialId);

        return trackingRecordRepository.getList(commercial);
    }

    @RequestMapping("/media-sources")
    public List<String> getMediaSourcesList() {
        return trackingRecordRepository.getMediaSources();
    }

    @RequestMapping("/offer-ids")
    public List<String> getOfferIds() {
        return trackingRecordRepository.getOfferIds();
    }

    @RequestMapping("/field-values")
    public Map<String, Set<String>> getFieldValues() {
        return fieldValueRegistry.getDictionary();
    }
}
