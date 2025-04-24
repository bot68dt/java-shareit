package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;
    private final Validator validator;

    @GetMapping
    public ResponseEntity<Object> getItemsById(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all items of the user with ID {} received.", userId);
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long itemId) {
        log.info("Request to get item with ID {} received.", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(name = "text") String text) {
        log.info("Request to get item containing text:  {} received.", text);
        return itemClient.searchItemByText(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) throws BadRequestException {
        log.info("Request to create new item received: {}", itemDto);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto, @PathVariable long itemId) throws BadRequestException {
        log.info("Request to update item received: {}", itemDto);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> comment(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody CommentDto commentDto, @PathVariable long itemId) throws BadRequestException {
        log.info("Request to comment item received: {}", commentDto);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return itemClient.comment(userId, commentDto, itemId);
    }
}