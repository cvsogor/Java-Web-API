package com.aditor.bi.commercials.domain.exception;

public class ConditionAlreadyExistsException extends DomainException {
    public ConditionAlreadyExistsException() {
        super();
    }
    public ConditionAlreadyExistsException(String message) {
        super(message);
    }
}
