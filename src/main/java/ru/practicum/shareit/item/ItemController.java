package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Checkers;
import ru.practicum.shareit.exception.IncorrectStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

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
    public List<ItemDto> getUserItems(@RequestHeader("X-Later-User-Id") Long userId) {
        return itemService.getUserItems(userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader ("X-Later-User-Id") Long userId, @RequestBody NewItemDto newItemDto,
                              BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        checkStatus(newItemDto.getStatus().getName());
        return itemService.addItem(userId, newItemDto);
    }

    @PatchMapping
    public ItemDto updateItem(@RequestHeader ("X-Later-User-Id") Long userId, @RequestBody UpdateItemDto itemDto,
                              BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        checkStatus(itemDto.getStatus().getName());
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    private void checkStatus(String status) {
        if (!(status.equals("FREE") ||
                status.equals("BUSY"))) {
            throw new IncorrectStatusException("Статус должен быть FREE или BUSY");
        }
    }
}
