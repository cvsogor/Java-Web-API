package com.aditor.bi.commercials.persistence;

import com.aditor.bi.commercials.domain.TargetCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetConditionRepository extends JpaRepository<TargetCondition, Long> {

}
