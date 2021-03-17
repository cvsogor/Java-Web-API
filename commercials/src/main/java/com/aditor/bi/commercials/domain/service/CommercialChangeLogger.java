package com.aditor.bi.commercials.domain.service;

import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.CommercialLogEntry;
import com.aditor.bi.commercials.persistence.CommercialLogEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommercialChangeLogger {
    private final Logger logger = LoggerFactory.getLogger(CommercialChangeLogger.class);

    private CommercialLogEntryRepository logEntryRepository;
    private CommercialChangeLogGenerator logEntryGenerator;

    @Autowired
    public CommercialChangeLogger(CommercialLogEntryRepository logEntryRepository, CommercialChangeLogGenerator logEntryGenerator) {
        this.logEntryRepository = logEntryRepository;
        this.logEntryGenerator = logEntryGenerator;
    }

    public void logChanges(Commercial oldCommercial, Commercial newCommercial) {
        List<CommercialLogEntry> logEntries = logEntryGenerator.generateLog(oldCommercial, newCommercial);
        if(!logEntries.isEmpty()) {
            logger.info("Detected change in commercial #" + oldCommercial.getId() + "; writing changes to log table.");
            logEntryRepository.save(logEntries);
            logEntryRepository.flush();
        }
    }
}
