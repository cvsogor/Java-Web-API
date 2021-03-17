package com.aditor.bi.commercials.test.unit;

import com.aditor.bi.commercials.domain.*;
import com.aditor.bi.commercials.domain.service.CommercialChangeLogGenerator;
import com.aditor.bi.commercials.domain.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.util.List;

/**
 * Created by ivan on 3/17/16.
 */
public class CommercialChangeLogGeneratorTest {
    private CommercialChangeLogGenerator commercialChangeLogGenerator = new CommercialChangeLogGenerator();
    private ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    public void testSimpleChange() throws Exception {
        Commercial left = new Commercial.Builder()
                .withActiveTarget()
                .withOfferId("A")
                .withMediaSource("1")
                .withStartDate(DateUtils.parseDate("2014-01-01"))
                .withEndDate(DateUtils.parseDate("2016-05-05"))
                .withType(CommercialType.PAYOUT)
                .withEventType(EventType.INSTALL)
                .withCondition(Operator.EQUALS, "A", "1", false)
                .withCondition(Operator.EQUALS, "B", "1", false)
                .withCondition(Operator.EQUALS, "D", "1", false)
                .build();

        Commercial right = new Commercial.Builder()
                .withActiveTarget()
                .withOfferId("D")
                .withMediaSource("2")
                .withStartDate(DateUtils.parseDate("2014-01-01"))
                .withEndDate(DateUtils.parseDate("2016-01-05"))
                .withType(CommercialType.REVENUE)
                .withEventType(EventType.FIRST_DEPOSIT)
                .withCondition(Operator.EQUALS, "B", "2", false)
                .withCondition(Operator.EQUALS, "C", "1", false)
                .withCondition(Operator.EQUALS, "D", "1", false)
                .build();

        right.getTarget().setActive(false);

        Commercial emptyRight = new Commercial();
        emptyRight.setTarget(new Target());

        List<CommercialLogEntry> commercialLogEntries = commercialChangeLogGenerator.generateLog(left, right);

        printObject(commercialLogEntries);
        printObject(commercialChangeLogGenerator.generateLog(left, emptyRight));


    }

    public void printObject(Object object) throws Exception {
        System.out.println(objectMapper.writeValueAsString(object));
    }
}
