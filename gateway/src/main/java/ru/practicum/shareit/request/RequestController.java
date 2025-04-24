package ru.practicum.shareit.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Set;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;
    private final Validator validator;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequestDto itemRequestDto) throws BadRequestException {
        log.info("Request to create new itemRequest received: {}", itemRequestDto);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return requestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestByRequestId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @PathVariable long requestId) {
        log.info("Request to get itemRequest with ID {} received.", requestId);
        return requestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all itemRequests of the user with ID {} received.", userId);
        return requestClient.getItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsByOthers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Request to get all itemRequests of the others");
        return requestClient.getItemRequestsByOthers(userId);
    }
}
