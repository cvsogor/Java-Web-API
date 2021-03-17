package com.aditor.dashboard.spring.validation;

public class MandatoryColumnsMissingException extends RuntimeException {
    public MandatoryColumnsMissingException() {
        super();
    }

    public MandatoryColumnsMissingException(String message) {
        super(message);
    }

    public MandatoryColumnsMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MandatoryColumnsMissingException(Throwable cause) {
        super(cause);
    }

    protected MandatoryColumnsMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
