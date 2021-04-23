package com.idvsbruck.pplflw.api.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handle(final Exception exception) {
        var message = "";
        if (exception instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        } else {
            message = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = {JsonParseException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handle(final JsonParseException exception) {
        return new ResponseEntity<>(exception.getOriginalMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = MismatchedInputException.class)
    public ResponseEntity<String> handle(final MismatchedInputException exception) {
        return new ResponseEntity<>(exception.getOriginalMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<String> handle(final MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handle(final HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultHandle(final Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
