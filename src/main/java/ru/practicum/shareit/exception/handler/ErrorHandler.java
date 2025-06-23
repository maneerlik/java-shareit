package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        logError(e);
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ErrorResponse.builder(status.value(), status.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailExists(EmailAlreadyExistsException e) {
        logError(e);
        HttpStatus status = HttpStatus.CONFLICT;
        return ErrorResponse.builder(status.value(), status.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logError(e);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ErrorResponse.builder(status.value(), status.getReasonPhrase())
                .message(e.getMessage())
                .validationErrors(errors)
                .build();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingHeader(MissingRequestHeaderException e) {
        logError(e);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ErrorResponse.builder(status.value(), status.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(Exception e) {
        logError(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ErrorResponse.builder(status.value(), status.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    private void logError(Exception e) {
        String template = """
            \n================================================= ERROR =================================================
            Message: {}
            Exception type: {}
            """;

        log.error(template, e.getMessage(), e.getClass().getName(), e);
    }
}
