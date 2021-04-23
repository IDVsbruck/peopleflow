package com.idvsbruck.pplflw.api.exceptions;

import org.springframework.http.HttpStatus;

public class PplflwStatemachineException extends PplflwException {

    public static final HttpStatus ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public PplflwStatemachineException(String message, Object... objects) {
        super(String.format(message, objects));
    }

    @Override
    public String getPplflwErrorCode() {
        return PplflwErrorCodes.STATEMACHINE_OPERATION.name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return ERROR_STATUS;
    }

    public static class Messages {

        public static final String PERSIST_STATE_ERROR = "State [%s] for employee [%s] cannot be persisted";
        public static final String NOT_PERSISTED_STATE = "State for employee [%s] is not persisted";
        public static final String CHECK_ERROR = "Employee cannot be approved, current state is [%s]";
    }
}
