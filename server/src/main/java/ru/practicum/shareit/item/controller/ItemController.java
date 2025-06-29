package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Запрос вещей пользователя с id {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("{itemId}")
    public ItemWithBookingsDto getItem(@RequestHeader(HEADER_USER_ID) Long userId, @PathVariable("itemId") Long itemId) {
        log.info("Запрос вещи с id {}", itemId);
        return itemService.getItem(itemId, userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader (HEADER_USER_ID) Long userId, @RequestBody NewItemDto newItemDto) {
        log.info("Создание вещи");
        return itemService.addItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader (HEADER_USER_ID) Long userId, @PathVariable Long itemId,
                              @RequestBody UpdateItemDto itemDto) {
        log.info("Обновление вещи");
        itemDto.setId(itemId);
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        log.info("Поиск вещи {}", text);
        if (text == null || text.isEmpty()) return Collections.emptyList();
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader (HEADER_USER_ID) Long userId, @PathVariable Long itemId,
                                 @RequestBody NewCommentDto newCommentDto) {
        log.info("Добавляем коммент");
        log.info("Валидация прошла успешно");
        return itemService.addComment(userId, itemId, newCommentDto);
    }
}
