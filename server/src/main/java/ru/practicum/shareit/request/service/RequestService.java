package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto createRequest(Long userId, NewRequestDto newRequestDto);

    Page<ItemRequestWithResponseDto> getUserRequests(Long userId, Integer from, Integer size);

    List<ItemRequestWithResponseDto> getAllRequests(Long userId);

    ItemRequestWithResponseDto getRequestById(Long requestId);
}
