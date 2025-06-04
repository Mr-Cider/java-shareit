package ru.practicum.shareit.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private final ZonedDateTime timestamp;
    private final Integer status;
    private final String error;
    private final String path;

    public ErrorResponse(String error, String path, Integer status) {
        this.timestamp = ZonedDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
