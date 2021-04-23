package com.idvsbruck.pplflw.api.exceptions;

import org.springframework.http.HttpStatus;

public class PplflwEmployeeException extends PplflwException {

    public static final HttpStatus ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public PplflwEmployeeException(String message, Object... objects) {
        super(String.format(message, objects));
    }

    @Override
    public String getPplflwErrorCode() {
        return PplflwErrorCodes.EMPLOYEE_ERROR.name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return ERROR_STATUS;
    }

    public static class Messages {

        public static final String NOT_FOUND = "An employee with email [%s] not found";
    }
}
