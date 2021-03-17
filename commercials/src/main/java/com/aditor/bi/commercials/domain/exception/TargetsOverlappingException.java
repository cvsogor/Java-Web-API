package com.aditor.bi.commercials.domain.exception;

import com.aditor.bi.commercials.domain.Target;

public class TargetsOverlappingException extends DomainException {
    private Target newTarget;
    private Target conflictingTarget;

    public TargetsOverlappingException() {
        super();
    }
    public TargetsOverlappingException(String message) {
        super(message);
    }

    public TargetsOverlappingException(String message, Target newTarget) {
        super(message);
        this.newTarget = newTarget;
    }

    public TargetsOverlappingException(String message, Target newTarget, Target conflictingTarget) {
        this(message, newTarget);
        this.conflictingTarget = conflictingTarget;
    }

    public Target getNewTarget() {
        return newTarget;
    }

    public Target getConflictingTarget() {
        return conflictingTarget;
    }
}
