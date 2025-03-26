package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.net.URI;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Collection<Item>> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all items of the user with ID {} received.", userId);
        return ResponseEntity.ok(itemService.getItemsByUserId(userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable long itemId) {
        log.info("Request to get item with ID {} received.", itemId);
        return ResponseEntity.ok(itemService.getItemById(userId, itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<Item>> searchItemByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(value = "text") String text) {
        log.info("Request to get item containing text:  {} received.", text);
        return ResponseEntity.ok(itemService.searchItemByText(userId, text));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemDto itemDto) {
        log.info("Request to create new item received: {}", itemDto);
        Item createdItem = itemService.createItem(userId, itemDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdItem.getId()).toUri();
        log.info("New item created with ID {}", createdItem.getId());
        return ResponseEntity.created(location).body(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemDto itemDto,
                                       @PathVariable long itemId) {
        log.info("Request to update item received: {}", itemDto);
        Item updatedItem = itemService.updateItem(userId, itemDto, itemId);
        log.info("Item with ID {} updated", itemId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedItem);
    }
}