package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader (HEADER_USER_ID) Long userId,
                                        @RequestBody NewRequestDto newRequestDto) {
        log.info("Создаем новый запрос");
        return requestService.createRequest(userId, newRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithResponseDto> getUserRequests(@RequestHeader (HEADER_USER_ID) Long userId,
                                                            @RequestParam (defaultValue = "0") Integer from,
                                                            @RequestParam (defaultValue = "10") Integer size) {
        log.info("Получаем запросы пользователя с id {} ", userId);
        return requestService.getUserRequests(userId, from, size).getContent();
    }

    @GetMapping("/all")
    public List<ItemRequestWithResponseDto> getAllRequests(@RequestHeader (HEADER_USER_ID) Long userId) {
        log.info("Получаем все запросы других пользователей");
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponseDto getRequest(@PathVariable Long requestId) {
        log.info("Получаем запрос с id {} ", requestId);
        return requestService.getRequestById(requestId);
    }
}
