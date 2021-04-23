package com.idvsbruck.pplflw.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class PplflwException extends RuntimeException {

    public PplflwException(String message) {
        super(message);
    }

    public abstract String getPplflwErrorCode();
    public abstract HttpStatus getHttpStatus();
}
