package com.aditor.bi.commercials.domain.dto.mapper;

import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.dto.CommercialDTO;
import com.aditor.bi.commercials.domain.dto.TargetConditionDTO;
import org.springframework.stereotype.Service;

@Service
public class CommercialMapper {
    public Commercial fromDTO(CommercialDTO commercialDTO) {
        Commercial.Builder builder = new Commercial.Builder()
                .withOfferId(commercialDTO.offerId)
                .withMediaSource(commercialDTO.mediaSource)
                .withAmount(commercialDTO.amount)
                .withStartDate(commercialDTO.dateStart)
                .withEndDate(commercialDTO.dateEnd)
                .withEventType(commercialDTO.eventType)
                .withType(commercialDTO.type)
                .withTarget(commercialDTO.isActive);

        for(TargetConditionDTO conditionDTO : commercialDTO.conditions) {
            builder.withCondition(conditionDTO.operator,
                    conditionDTO.fieldName,
                    conditionDTO.fieldValue,
                    conditionDTO.isInverse);
        }

        return builder.build();
    }

    public CommercialDTO toDTO(Commercial commercial) {
        CommercialDTO dto = new CommercialDTO();
        dto.id = commercial.getId();
        dto.isActive = commercial.getTarget().isActive();
        dto.offerId = commercial.getOfferId();
        dto.mediaSource = commercial.getMediaSource();
        dto.type = commercial.getType();
        dto.eventType = commercial.getEventType();
        dto.dateStart = commercial.getDateStart();
        dto.dateEnd = commercial.getDateEnd();
        dto.amount = commercial.getAmount();
        dto.updatedOn = commercial.getUpdatedOn();

        commercial.getTarget().getConditions().forEach(condition -> {
            dto.conditions.add(new TargetConditionDTO(
                    condition.getOperator(),
                    condition.getFieldName(),
                    condition.getFieldValue(),
                    condition.getInverse()
            ));
        });

        return dto;
    }
}
