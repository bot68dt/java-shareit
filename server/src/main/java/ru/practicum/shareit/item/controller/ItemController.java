package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Collection<ItemTimingDto>> getItemsById(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, name = "from", defaultValue = "0") Integer from, @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Request to get all items of the user with ID {} received.", userId);
        return ResponseEntity.ok(itemService.getItemsByUserId(userId, from, size));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemTimingDto> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable long itemId) {
        log.info("Request to get item with ID {} received.", itemId);
        return ResponseEntity.ok(itemService.getItemById(userId, itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItemByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(value = "text") String text, @RequestParam(required = false, name = "from", defaultValue = "0") Integer from, @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Request to get item containing text:  {} received.", text);
        return ResponseEntity.ok(itemService.searchItemByText(userId, text, from, size));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemDto itemDto) {
        log.info("Request to create new item received: {}", itemDto);
        ItemDto createdItem = itemService.createItem(userId, itemDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdItem.getId()).toUri();
        log.info("New item created with ID {}", createdItem.getId());
        return ResponseEntity.created(location).body(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemDto itemDto,
                                       @PathVariable long itemId) {
        log.info("Request to update item received: {}", itemDto);
        ItemDto updatedItem = itemService.updateItem(userId, itemDto, itemId);
        log.info("Item with ID {} updated", itemId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedItem);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> comment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestBody CommentDto commentDto,
                                                      @PathVariable long itemId) {
        log.info("Request to comment item received: {}", commentDto);
        CommentResponseDto comment = itemService.comment(userId, commentDto, itemId);
        log.info("Item with ID {} updated", itemId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(comment);
    }
}