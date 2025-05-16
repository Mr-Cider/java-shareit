package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(AnnotationValidatorException e, WebRequest request) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIdValidationException(ValidationException e, WebRequest request) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e, WebRequest request) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectStatusException(IncorrectStatusException e, WebRequest request) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri",""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmailException(DuplicateEmailException e, WebRequest request) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri",""));
    }
}