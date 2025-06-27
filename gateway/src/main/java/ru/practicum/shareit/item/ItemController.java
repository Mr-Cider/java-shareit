package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(HEADER_USER_ID) @Positive long userId, @PathVariable("itemId") @Positive long itemId) {
        return itemClient.getItem(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader (HEADER_USER_ID) Long userId, @Valid @RequestBody NewItemDto newItemDto) {
        return itemClient.createItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader (HEADER_USER_ID) @Positive Long userId, @PathVariable @Positive Long itemId,
                              @Valid @RequestBody UpdateItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByNameOrDescription(@RequestParam String text) {
        return itemClient.searchItemsByNameOrDescription(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader (HEADER_USER_ID) @Positive Long userId, @PathVariable @Positive Long itemId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        return itemClient.addComment(userId, itemId, newCommentDto);
    }
}
