package com.aditor.bi.commercials.test.unit;


import com.aditor.bi.commercials.domain.Operator;
import com.aditor.bi.commercials.domain.TargetCondition;
import com.aditor.bi.commercials.domain.service.validation.TargetConditionOverlappingDetector;
import org.junit.Assert;
import org.junit.Test;

public class TargetConditionOverlappingDetectorTest {

    private TargetConditionOverlappingDetector detector = new TargetConditionOverlappingDetector();

    @Test
    public void testValidEquals() {
        TargetCondition left = fromString("A:1");
        TargetCondition right = fromString("A:2");

        Assert.assertTrue("Branching was not detected in equals conditions",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertTrue("Branching was not detected in reverse order equals conditions",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testInvalidEquals() {
        TargetCondition left = fromString("A:1");
        TargetCondition right = fromString("A:1");

        Assert.assertFalse("Branching detected in identical conditions",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertFalse("Branching detected in identical conditions",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testInverseEquals() {
        TargetCondition left = fromString("A:1");
        TargetCondition right = fromString("!A:1");

        Assert.assertTrue("Branching not detected in not-equals",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertTrue("Branching not detected in reverse order not-equals",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testValidInList() {
        TargetCondition left = fromString("A:1,2");
        TargetCondition right = fromString("A:3,4");

        Assert.assertTrue("Branching not detected in non-overlapping list",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertTrue("Branching not detected in reverse order non-overlapping list",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testInvalidInList() {
        TargetCondition left = fromString("A:2,3");
        TargetCondition right = fromString("A:3,4");

        Assert.assertFalse("Branching detected in overlapping list",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertFalse("Branching detected in reverse order overlapping list",
                detector.fieldValuesAreBranching(left, right));
    }

    @Test
    public void testInverseInList() {
        TargetCondition left = fromString("A:1,2");
        TargetCondition right = fromString("!A:3,4");

    }

    @Test
    public void testComplexInverseInList() {
        TargetCondition left = fromString("A:1,2");
        TargetCondition right = fromString("!A:3,5");


        Assert.assertFalse("Branching detected in complex inverse in list with partial overlapment",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertFalse("Branching detected in reverse order complex inverse in list with partial overlapment",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testInListEquals() {
        TargetCondition left = fromString("A:1");
        TargetCondition right = fromString("A:2,3");

        Assert.assertTrue("Branching not detected in overlapping in-list-equals",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertTrue("Branching not detected in reverse order overlapping in-list-equals",
                detector.fieldValuesAreBranching(right, left));
    }

    @Test
    public void testInvalidInListEquals() {
        TargetCondition left = fromString("A:2");
        TargetCondition right = fromString("A:1,2");

        Assert.assertFalse("Branching detected in overlapping in-list-equals",
                detector.fieldValuesAreBranching(left, right));

        Assert.assertFalse("Branching detected in reverse order overlapping in-list-equals",
                detector.fieldValuesAreBranching(right, left));
    }

    public TargetCondition fromString(String conditionString) {
        return fromString(conditionString, false);
    }

    public TargetCondition fromString(String conditionString, boolean forceInList) {
        TargetCondition condition = new TargetCondition();

        boolean isInverse = conditionString.charAt(0) == '!';
        Operator operator = conditionString.contains(",") || forceInList ? Operator.IN_LIST : Operator.EQUALS;

        if(isInverse) {
            conditionString = conditionString.substring(1);
        }

        String[] keyValuePair = conditionString.split(":");

        condition.setFieldName(keyValuePair[0]);
        condition.setFieldValue(keyValuePair[1]);
        condition.setInverse(isInverse);
        condition.setOperator(operator);

        return condition;
    }
}
