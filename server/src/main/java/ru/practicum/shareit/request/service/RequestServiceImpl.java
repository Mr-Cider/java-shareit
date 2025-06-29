package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.RequestDataTransformer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, NewRequestDto newRequestDto) {
        User user = checkUser(userId);
        return RequestDataTransformer.convertToItemRequestDto(requestRepository.save(
                RequestDataTransformer.convertNewRequest(user, newRequestDto)));
    }

    @Override
    public List<ItemRequestWithResponseDto> getUserRequests(Long userId) {
        List<ItemRequest> requests = requestRepository.getRequestsByRequestorId(userId);
        return getItemRequestListForResponse(requests);
    }

    @Override
    public List<ItemRequestWithResponseDto> getAllRequests(Long userId) {
        List<ItemRequest> requests = requestRepository.getOtherRequests(userId);
        return getItemRequestListForResponse(requests);
    }

    @Override
    public ItemRequestWithResponseDto getRequestById(Long requestId) {
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id + " + requestId + " не найден"));
        return RequestDataTransformer.convertToItemRequestWithResponseDto(getItemsFromRequest(requestId), request);
    }

    private User checkUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private List<ItemRequestWithResponseDto> getItemRequestListForResponse(List<ItemRequest> requests) {
        return requests.stream()
                .map(itemRequest -> RequestDataTransformer
                        .convertToItemRequestWithResponseDto(getItemsFromRequest(itemRequest.getId()), itemRequest))
                .collect(Collectors.toList());
    }

    private List<Item> getItemsFromRequest(Long requestId) {
        return itemStorage.findByRequest_Id(requestId);
    }
}
