function TargetCondition(operator, fieldName, fieldValue, isInverse) {
    this.operator = operator;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.isInverse = isInverse;
}