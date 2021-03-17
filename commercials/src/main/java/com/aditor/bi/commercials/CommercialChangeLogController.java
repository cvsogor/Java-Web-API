package com.aditor.bi.commercials;

import com.aditor.bi.commercials.domain.CommercialLogEntry;
import com.aditor.bi.commercials.persistence.CommercialLogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/commercials/{commercialId}/change-logs/")
@RestController
public class CommercialChangeLogController {

    @Autowired
    private CommercialLogEntryRepository repository;

    @RequestMapping("/")
    public List<CommercialLogEntry> getLogEntries(@PathVariable Long commercialId) {
        return repository.findByCommercialId(commercialId);
    }
}
