package com.aditor.bi.commercials.persistence;

import com.aditor.bi.commercials.domain.CommercialType;
import com.aditor.bi.commercials.domain.Commercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommercialRepository extends JpaRepository<Commercial, Long> {

    @Query("SELECT c FROM Commercial c " +
            "WHERE (c.offerId = ?1 AND (?2 IS NULL OR ?2 = '' OR c.mediaSource = ?2))" +
            "AND type = ?5 " +
            "AND ((c.dateStart <= ?3 AND c.dateEnd >= ?3) " +
            "OR (c.dateStart <= ?4 AND c.dateEnd >= ?4) " +
            "OR (c.dateStart >= ?3 AND c.dateEnd <= ?4)) ")
    List<Commercial> findMatchingCommercials(String offerId,
                                             String mediaSource,
                                             Date startDate,
                                             Date endDate,
                                             CommercialType type);

    @Query("SELECT c FROM Commercial c JOIN c.target t WHERE t.isActive = true")
    List<Commercial> findAllActive();
}
