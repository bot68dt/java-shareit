package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;
    private final Validator validator;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Request to get user with ID {} received.", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto userRequestDto) throws BadRequestException {
        log.info("Request to create new user received: {}", userRequestDto);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return userClient.addUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId, @RequestBody UserDto userRequestDto) throws BadRequestException {
        log.info("Request to update user received: {}", userRequestDto);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new BadRequestException("Error when creating new user");
        }
        return userClient.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Request to delete user with ID {} received.", userId);
        return userClient.deleteUser(userId);
    }
}
