package ru.practicum.shareit.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHendler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(AnnotationValidatorException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIdValidationException(ValidationException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectStatusException(IncorrectStatusException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri",""));
    }
}