package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Checkers;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        log.info("Запрос вещей пользователя с id {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable("itemId") @Positive Long itemId) {
        log.info("Запрос вещи с id {}", itemId);
        return itemService.getItem(itemId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader (HEADER_USER_ID) Long userId, @Valid @RequestBody NewItemDto newItemDto,
                              BindingResult bindingResult) {
        log.info("Создание вещи");
        Checkers.checkErrorValidation(bindingResult, log);
        log.trace("Валидация прошла успешно");
        return itemService.addItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader (HEADER_USER_ID) @Positive Long userId, @PathVariable @Positive Long itemId,
                              @Valid @RequestBody UpdateItemDto itemDto,
                              BindingResult bindingResult) {
        log.info("Обновление вещи");
        itemDto.setId(itemId);
        Checkers.checkErrorValidation(bindingResult, log);
        log.trace("Валидация прошла успешно");
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        if (text == null || text.isEmpty()) return Collections.emptyList();
        log.info("Поиск вещи {}", text);
        return itemService.searchItems(text);
    }
}
