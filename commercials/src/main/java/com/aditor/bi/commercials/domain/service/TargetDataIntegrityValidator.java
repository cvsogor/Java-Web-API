package com.aditor.bi.commercials.domain.service;

import com.aditor.bi.commercials.domain.Target;
import com.aditor.bi.commercials.domain.service.validation.TargetOverlappingDetector;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.persistence.TargetRepository;
import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.exception.TargetsOverlappingException;
import com.aditor.bi.commercials.persistence.TargetConditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TargetDataIntegrityValidator {

    private CommercialRepository commercialRepository;
    private TargetRepository targetRepository;
    private TargetConditionRepository targetConditionRepository;

    private Logger logger = LoggerFactory.getLogger(TargetDataIntegrityValidator.class);


    @Autowired
    TargetDataIntegrityValidator(CommercialRepository commercialRepository,
                                 TargetRepository targetRepository,
                                 TargetConditionRepository targetConditionRepository) {
        this.commercialRepository = commercialRepository;
        this.targetRepository = targetRepository;
        this.targetConditionRepository = targetConditionRepository;
    }

    public void validateTarget(Target target)
    {
        Commercial targetCommercial = target.getCommercial();
        List<Commercial> commercials = commercialRepository.findMatchingCommercials(targetCommercial.getOfferId(),
                targetCommercial.getMediaSource(),
                targetCommercial.getDateStart(),
                targetCommercial.getDateEnd(),
                targetCommercial.getType())
                .stream()
                .filter(commercial -> commercial.getTarget() != null)
                .filter(commercial -> commercial.getTarget().isActive())
                .filter(commercial -> !Objects.equals(commercial.getId(), targetCommercial.getId()))
                .collect(Collectors.toList());

        if(commercials.isEmpty())
            return;

        boolean similarEmptyTargetExists = commercials.stream()
                .map(Commercial::getTarget)
                .filter(Target::isActive)
                .anyMatch(target1 -> target1.getConditions().isEmpty());

        if(target.getConditions().isEmpty() && similarEmptyTargetExists)
            throw new TargetsOverlappingException("Conflicting empty target exists", target);

        if(target.getConditions().isEmpty())
            return;

        TargetOverlappingDetector collisionDetector = new TargetOverlappingDetector();

        for(Target existingTarget : commercials.stream().map(Commercial::getTarget).collect(Collectors.toList())) {
            if(collisionDetector.areConflicting(target, existingTarget)) {
                throw new TargetsOverlappingException("Targets are conflicting", target, existingTarget);
            }
        }
    }
}
