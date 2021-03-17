package com.aditor.bi.commercials.test.functional;

import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.CommercialType;
import com.aditor.bi.commercials.Application;
import com.aditor.bi.commercials.domain.service.TargetDataIntegrityValidator;
import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.EventType;
import com.aditor.bi.commercials.domain.Operator;
import com.aditor.bi.commercials.domain.exception.TargetsOverlappingException;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@ActiveProfiles(BuildProfiles.TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TargetDataIntegrityValidatorTest {

    @Autowired
    TargetDataIntegrityValidator validator;

    @Autowired
    CommercialRepository commercialRepository;

    private static final String offerId = "1000";
    private static final String mediaSource = "TestMediaSource";
    private static final Date dateStart = Date.from(LocalDate.of(2016, 1, 1).atStartOfDay(ZoneId.of("UTC")).toInstant());
    private static final Date dateEnd = Date.from(LocalDate.of(2016, 5, 1).atStartOfDay(ZoneId.of("UTC")).toInstant());
    private static final EventType eventType = EventType.FIRST_DEPOSIT;
    private static final CommercialType commercialType = CommercialType.PAYOUT;
    private static List<Commercial> defaultCommercials = new ArrayList<>();


    public Commercial.Builder buildDefaultTarget() {
        return new Commercial.Builder()
                .withOfferId(offerId)
                .withMediaSource(mediaSource)
                .withActiveTarget()
                .withStartDate(dateStart)
                .withEndDate(dateEnd)
                .withEventType(eventType)
                .withType(commercialType);
    }

    public void fillCommercialsData() throws Exception {
        commercialRepository.deleteAll();

        Resource commercials_1 = new ClassPathResource("commercials_1.txt");
        Scanner scanner = new Scanner(commercials_1.getFile());

        List<String> patterns = new LinkedList<>();

        while(scanner.hasNext()) {
            patterns.add(scanner.nextLine());
        }

        for(String pattern : patterns) {
            Commercial.Builder builder = new Commercial.Builder()
                    .withActiveTarget()
                    .withOfferId(offerId)
                    .withMediaSource(mediaSource)
                    .withStartDate(dateStart)
                    .withEndDate(dateEnd)
                    .withType(commercialType)
                    .withEventType(eventType);

            for(String subPattern : pattern.split(",")) {
                String[] subPatternComponents = subPattern.split(":");
                String key = subPatternComponents[0];
                String value = subPatternComponents[1];

                builder.withCondition(Operator.EQUALS, key, value, false);
            }

            Commercial temp = builder.build();

            defaultCommercials.add(commercialRepository.save(temp));
        }

        commercialRepository.flush();
    }

    @Test
    public void testAddEmptyTargetToEmptySet() throws Exception {
        commercialRepository.deleteAll();

        Commercial commercial = buildDefaultTarget()
                .build();
        try {
            validator.validateTarget(commercial.getTarget());
        } catch (TargetsOverlappingException exception) {
            assert false;
        }
    }

    @Test
    public void testAddValidEmptyTarget() throws Exception {
        fillCommercialsData();

        Commercial commercial = buildDefaultTarget()
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
        } catch (TargetsOverlappingException exception) {
            assert false;
        }
    }

    @Test
    public void testAddDuplicateTarget() throws Exception {
        fillCommercialsData();

        Commercial commercial = buildDefaultTarget()
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "B", "1", false)
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
            assert false;
        } catch (TargetsOverlappingException exception) {
            assert true;
        }
    }

    @Test
    public void testAddValidMoreSpecificTarget() throws Exception {
        fillCommercialsData();

        Commercial commercial = buildDefaultTarget()
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "B", "1", false)
                .withCondition(Operator.EQUALS, "C", "1", false)
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
            assert true;
        } catch (TargetsOverlappingException exception) {
            assert false;
        }
    }

    @Test
    public void testAddValidBranchTarget() throws Exception {
        fillCommercialsData();

        Commercial commercial = buildDefaultTarget()
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "B", "2", false)
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
            assert true;
        } catch (TargetsOverlappingException exception) {
            assert false;
        }
    }

    @Test
    public void testAddInvalidBranchTarget() throws Exception {
        fillCommercialsData();

        Commercial commercial = buildDefaultTarget()
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "C", "1", false)
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
            assert false;
        } catch (TargetsOverlappingException exception) {
            assert true;
        }
    }

    @Test
    public void testAddInvalidTargetToDifferentOffer() throws Exception {
        fillCommercialsData();

        Commercial commercial = new Commercial.Builder()
                .withActiveTarget()
                .withOfferId(offerId + 1337L)
                .withMediaSource(mediaSource)
                .withStartDate(dateStart)
                .withEndDate(dateEnd)
                .withType(commercialType)
                .withEventType(eventType)
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "B", "1", false)
                .build();

        try {
            validator.validateTarget(commercial.getTarget());
            assert true;
        } catch (TargetsOverlappingException exception) {
            assert false;
        }
    }
}
