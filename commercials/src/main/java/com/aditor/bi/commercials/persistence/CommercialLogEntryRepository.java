package com.aditor.bi.commercials.persistence;

import com.aditor.bi.commercials.domain.CommercialLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommercialLogEntryRepository extends JpaRepository<CommercialLogEntry, Long> {
    public List<CommercialLogEntry> findByCommercialId(Long commercialId);
}
