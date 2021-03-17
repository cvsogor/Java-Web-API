package com.aditor.bi.commercials.tracking;


import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TrackingFieldValueRegistry {
    private Map<String, Set<String>> dictionary = new HashMap<>();
    private TrackingRecordRepository trackingRecordRepository;
    private final Logger logger = LoggerFactory.getLogger(TrackingFieldValueRegistry.class);
    private final ObjectMapper mapper;

    @Autowired
    public TrackingFieldValueRegistry(TrackingRecordRepository trackingRecordRepository) {
        this.trackingRecordRepository = trackingRecordRepository;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Scheduled(fixedRate = 3600000)
    public void reload() {
        dictionary = trackingRecordRepository.getUniqueFieldValues();
        try {
            logger.info("Updated fieldValue dictionary: " + mapper.writeValueAsString(dictionary));
        } catch (Exception exception) {
            logger.error("Failed to serialize dictionary: ", exception);
        }
    }

    public Map<String, Set<String>> getDictionary() {
        return dictionary;
    }


}
