package ru.practicum.shareit.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithCorrectFields() {
        String error = "Validation failed";
        String path = "/items";
        int status = 400;
        ErrorResponse response = new ErrorResponse(error, path, status);
        ZonedDateTime testStart = ZonedDateTime.now().minusSeconds(2);
        ZonedDateTime testEnd = ZonedDateTime.now().plusSeconds(2);
        assertThat(response.getError()).isEqualTo(error);
        assertThat(response.getPath()).isEqualTo(path);
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getTimestamp())
                .isAfterOrEqualTo(testStart)
                .isBeforeOrEqualTo(testEnd);
    }
}