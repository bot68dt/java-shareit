package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequest> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Request to create new itemRequest received: {}", itemRequestDto);
        ItemRequest itemRequest = itemRequestService.createRequest(userId, itemRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemRequest.getId()).toUri();
        log.info("New booking created created with ID {}", itemRequest.getId());
        return ResponseEntity.created(location).body(itemRequest);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequest> getItemRequestByRequestId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @PathVariable long requestId) {
        log.info("Request to get itemRequest with ID {} received.", requestId);
        return ResponseEntity.ok(itemRequestService.getItemRequestById(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<Collection<ItemRequest>> getAllItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all itemRequests of the user with ID {} received.", userId);
        return ResponseEntity.ok(itemRequestService.getItemRequestsByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ItemRequest>> getAllItemRequestsByOthers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all itemRequests of the others");
        return ResponseEntity.ok(itemRequestService.getItemRequestsByOthers(userId));
    }

}
