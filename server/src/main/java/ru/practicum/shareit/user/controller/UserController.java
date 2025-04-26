package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        log.info("Request to get user with ID {} received.", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userRequestDto) {
        log.info("Request to create new user received: {}", userRequestDto);
        UserDto createdUser = userService.addUser(userRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getId()).toUri();
        log.info("New user created with ID {}", createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable long userId, @RequestBody UserDto userRequestDto) {
        log.info("Request to update user received: {}", userRequestDto);
        UserDto updatedUser = userService.updateUser(userId, userRequestDto);
        log.info("User with ID {} updated", updatedUser.getId());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        log.info("Request to delete user with ID {} received.", userId);
        userService.deleteUser(userId);
        log.info("User with ID {} deleted.", userId);
        return ResponseEntity.noContent().build();
    }
}