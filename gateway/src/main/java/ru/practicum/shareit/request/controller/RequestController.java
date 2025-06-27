package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.NewRequestDto;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestClient requestClient;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> createRequest(@Positive @RequestHeader (HEADER_USER_ID) Long userId,
                                        @Valid @RequestBody NewRequestDto newRequestDto) {
        return requestClient.createRequest(userId, newRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@Positive @RequestHeader (HEADER_USER_ID) Long userId) {
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@Positive @RequestHeader (HEADER_USER_ID) Long userId) {
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@Positive @PathVariable Long requestId) {
        return requestClient.getRequestById(requestId);
    }
}
