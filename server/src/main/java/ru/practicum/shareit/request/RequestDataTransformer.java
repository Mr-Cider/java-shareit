package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemDataTransformer;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDataTransformer;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestDataTransformer {
    public static ItemRequest convertNewRequest(User requestor, NewRequestDto newRequestDto) {
        return ItemRequest.builder()
                .description(newRequestDto.getDescription())
                .requestor(requestor)
                .requestDate(LocalDateTime.now())
                .build();
    }

    public static ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(UserDataTransformer.convertToUserDto(itemRequest.getRequestor()))
                .requestDate(itemRequest.getRequestDate())
                .build();
    }

    public static ItemRequestWithResponseDto convertToItemRequestWithResponseDto(List<Item> items, ItemRequest itemRequest) {
        return ItemRequestWithResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(UserDataTransformer.convertToUserDto(itemRequest.getRequestor()))
                .requestDate(itemRequest.getRequestDate())
                .items(convertToListItemForRequestDto(items))
                .build();
    }

    private static List<ItemForRequestDto> convertToListItemForRequestDto(List<Item> items) {
        return items.stream().map(ItemDataTransformer::convertToItemForRequestDto).collect(Collectors.toList());
    }
}
