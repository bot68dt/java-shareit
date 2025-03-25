package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId) {
        log.info("Request to get user with ID {} received.", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto newUserRequest) {
        log.info("Request to create new user received: {}", newUserRequest);
        User createdUser = userService.addUser(newUserRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getId()).toUri();
        log.info("New user created with ID {}", createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable long userId, @RequestBody UserDto updateUserRequest) {
        log.info("Request to update user received: {}", updateUserRequest);
        User updatedUser = userService.updateUser(userId, updateUserRequest);
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