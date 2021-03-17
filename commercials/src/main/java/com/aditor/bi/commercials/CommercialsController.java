package com.aditor.bi.commercials;

import com.aditor.bi.commercials.domain.*;
import com.aditor.bi.commercials.domain.dto.CommercialDTO;
import com.aditor.bi.commercials.domain.dto.mapper.CommercialMapper;
import com.aditor.bi.commercials.domain.exception.ConditionAlreadyExistsException;
import com.aditor.bi.commercials.domain.exception.InactiveCommercialException;
import com.aditor.bi.commercials.domain.exception.TargetsOverlappingException;
import com.aditor.bi.commercials.domain.service.CommercialChangeLogger;
import com.aditor.bi.commercials.domain.service.TargetDataIntegrityValidator;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.persistence.TrackingRecordRepository;
import com.aditor.bi.commercials.security.PermissionChecker;
import com.aditor.bi.commercials.tracking.CommercialCostsCalculator;
import com.aditor.bi.commercials.tracking.CommercialCostsUpdater;
import com.aditor.bi.commercials.tracking.CommercialUpdateNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: Handle change during in-progress recalculation
 */
@RequestMapping("/api/commercials")
@RestController
public class CommercialsController {
    private final Logger logger = LoggerFactory.getLogger(CommercialsController.class);

    @Autowired
    CommercialRepository repository;

    @Autowired
    TargetDataIntegrityValidator validator;

    @Autowired
    CommercialMapper mapper;

    @Autowired
    CommercialCostsUpdater costsUpdater;

    @Autowired
    CommercialUpdateNotificationService updateNotificationService;

    @Autowired
    CommercialChangeLogger changeLogger;

    @Autowired
    PermissionChecker permissionChecker;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<CommercialDTO> getList(@AuthenticationPrincipal User currentUser)
    {
        return repository.findAll().stream()
                .filter(commercial -> permissionChecker.isAllowedTo(commercial, currentUser, ActionType.READ))
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{commercialId}", method = RequestMethod.GET)
    public CommercialDTO get(@PathVariable Long commercialId, @AuthenticationPrincipal User currentUser)
    {
        Commercial commercial = repository.findOne(commercialId);
        if(!permissionChecker.isAllowedTo(commercial, currentUser, ActionType.READ)) {
            throw new AccessDeniedException("User is not allowed to create commercials with this offerId / mediaSource");
        }
        return mapper.toDTO(commercial);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public CommercialDTO create(@RequestBody CommercialDTO commercialDTO, @AuthenticationPrincipal User currentUser)
    {
        Commercial commercial = mapper.fromDTO(commercialDTO);

        if(!permissionChecker.isAllowedTo(commercial, currentUser, ActionType.CREATE)) {
            throw new AccessDeniedException("User is not allowed to create commercials with this offerId / mediaSource");
        }

        if(commercial.isActive()) {
            validator.validateTarget(commercial.getTarget());
        }

        repository.saveAndFlush(commercial);

        if(commercial.isActive()) {
            updateCommercialCosts(commercial);
            sendCommercialCostsUpdate(commercial);
        }

        return mapper.toDTO(commercial);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{commercialId}", method = RequestMethod.PUT)
    public CommercialDTO edit(@PathVariable Long commercialId, @RequestBody CommercialDTO commercialDTO, @AuthenticationPrincipal User currentUser)
    {
        Commercial commercial = mapper.fromDTO(commercialDTO);
        commercial.setId(commercialId);
        Commercial oldCommercial = repository.findOne(commercialId);

        if(!permissionChecker.isAllowedTo(commercial, currentUser, ActionType.CREATE) ||
                !permissionChecker.isAllowedTo(oldCommercial, currentUser, ActionType.UPDATE)) {
            throw new AccessDeniedException("User is not allowed to update commercials with this offerId / mediaSource");
        }

        if(commercial.isActive()) {
            validator.validateTarget(commercial.getTarget());

            resetCommercialCosts(oldCommercial);
            sendCommercialCostsUpdate(oldCommercial);
        }
        changeLogger.logChanges(oldCommercial, commercial);

        commercial = repository.saveAndFlush(commercial);

        if(commercial.isActive()) {
            updateCommercialCosts(commercial);
            sendCommercialCostsUpdate(commercial);
        }

        return mapper.toDTO(commercial);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{commercialId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long commercialId, @AuthenticationPrincipal User currentUser) {
        Commercial commercial = repository.findOne(commercialId);

        if(!permissionChecker.isAllowedTo(commercial, currentUser, ActionType.DELETE)) {
            throw new AccessDeniedException("User is not allowed to delete commercials with this offerId / mediaSource");
        }

        resetCommercialCosts(commercial);
        sendCommercialCostsUpdate(commercial);

        repository.delete(commercialId);
        repository.flush();
    }

    @RequestMapping(value = "/{commercialId}/copy", method = RequestMethod.POST)
    public Commercial closeAndCopy(@PathVariable Long commercialId, @AuthenticationPrincipal User currentUser) {
        Commercial commercial = repository.findOne(commercialId);
        if(!permissionChecker.isAllowedTo(commercial, currentUser, ActionType.UPDATE) ||
                !permissionChecker.isAllowedTo(commercial, currentUser, ActionType.CREATE)) {
            throw new AccessDeniedException("User is not allowed copy commercials with this offerId / mediaSource");
        }

        Commercial copy = new Commercial(commercial);

        commercial.setDateEnd(new Date());
        commercial.getTarget().setActive(false);
        repository.saveAndFlush(commercial);

        return repository.saveAndFlush(copy);
    }

    private void updateCommercialCosts(Commercial commercial) {
        costsUpdater.update(commercial);
    }

    private void resetCommercialCosts(Commercial commercial) {
        costsUpdater.reset(commercial);
    }

    private void sendCommercialCostsUpdate(Commercial commercial) {
        updateNotificationService.notify(commercial);
    }


    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        logger.info("NPE Error: ", exception);

        return "\"" + exception.getMessage() + "\"";
    }

    @ExceptionHandler(ConditionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String conditionAlreadyExistsExceptionHandler(ConditionAlreadyExistsException exception) {
        return "\"" + exception.getMessage() + "\"";
    }

    @ExceptionHandler(TargetsOverlappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String targetsOverlappingExceptionHandler(TargetsOverlappingException exception) {
        return "\"" + exception.getMessage() + "\"";
    }

    @ExceptionHandler(InactiveCommercialException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String inactiveCommercialExceptionHandler(InactiveCommercialException exception) {
        return "\"" + exception.getMessage() + "\"";
    }
}
