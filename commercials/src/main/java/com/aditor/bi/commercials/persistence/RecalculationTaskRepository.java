package com.aditor.bi.commercials.persistence;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.RecalculationStatus;
import com.aditor.bi.commercials.domain.RecalculationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecalculationTaskRepository extends JpaRepository<RecalculationTask, Long> {
    public List<RecalculationTask> findAllByCommercial(Commercial commercial);

    @Query("SELECT t FROM RecalculationTask t WHERE t.commercial = ?1 AND t.status = ?2")
    public List<RecalculationTask> findAllByStatusAndCommercial(Commercial commercial, RecalculationStatus status);

    @Query("SELECT t FROM RecalculationTask t WHERE t.status = 'IN_PROGRESS'")
    public List<RecalculationTask> findAllInProgress();
}
