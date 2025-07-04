package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.NewRequestDto;

@Controller
@RequestMapping("/requests")
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
    public ResponseEntity<Object> getUserRequests(@Positive @RequestHeader (HEADER_USER_ID) Long userId,
                                                  @PositiveOrZero @RequestParam (defaultValue = "0") Integer from,
                                                  @Positive @RequestParam (defaultValue = "10") Integer size) {
        return requestClient.getUserRequests(userId, from, size);
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
