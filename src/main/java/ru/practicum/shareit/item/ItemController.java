package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Checkers;
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
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос вещей пользователя с id {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        log.info("Запрос вещи с id {}", itemId);
        return itemService.getItem(itemId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader ("X-Sharer-User-Id") Long userId, @Valid @RequestBody NewItemDto newItemDto,
                              BindingResult bindingResult) {
        log.info("Создание вещи");
        Checkers.checkErrorValidation(bindingResult, log);
        log.trace("Валидация прошла успешно");
        return itemService.addItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader ("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
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
