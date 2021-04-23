package com.idvsbruck.pplflw.api.exceptions;

import org.springframework.http.HttpStatus;

public class PplflwUnexpectedException extends PplflwException {

    public static final HttpStatus ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public PplflwUnexpectedException(String message, Object... objects) {
        super(String.format(message, objects));
    }

    @Override
    public String getPplflwErrorCode() {
        return PplflwErrorCodes.UNEXPECTED_ERROR.name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return ERROR_STATUS;
    }

    public static class Messages {

        public static final String CONVERSATION_ERROR = "Error object converstaion";
    }
}
