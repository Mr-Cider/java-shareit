package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto createRequest(Long userId, NewRequestDto newRequestDto);

    List<ItemRequestWithResponseDto> getUserRequests(Long userId);

    List<ItemRequestWithResponseDto> getAllRequests(Long userId);

    ItemRequestWithResponseDto getRequestById(Long requestId);
}
