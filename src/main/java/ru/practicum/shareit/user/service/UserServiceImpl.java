package ru.practicum.shareit.user.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserCreationException;
import ru.practicum.shareit.user.exception.UserEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserStorage userStorage;
    private final Validator validator;
    private final UserMapper mapper;

    @Override
    public User getUserById(long id) {
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", id);
            throw new UserNotFoundException("Error when getting user", id);
        }
        log.debug("Getting user with ID {}", id);
        return user.get();
    }

    @Override
    public User addUser(UserDto newUserRequest) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(newUserRequest);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new UserValidationException("Error when creating new user",
                    violations.iterator().next().getMessage());
        }
        if (newUserRequest.getName() == null) {
            log.debug("User had no name");
            throw new UserCreationException("Error when creating new user",
                    "User has no name");
        }
        if (newUserRequest.getEmail() == null) {
            log.debug("User had no email");
            throw new UserCreationException("Error when creating new user",
                    "User has no email");
        } else if (!userStorage.getUserByEmail(newUserRequest.getEmail()).isEmpty()) {
            log.debug("User had duplicated email");
            throw new UserEmailException("Error when creating new user",
                    "User has duplicated email");
        }
        long userId = userStorage.addUser(newUserRequest);
        log.debug("Adding new user {}", newUserRequest);
        return getUserById(userId);
    }

    @Override
    public User updateUser(long id, UserDto updateUserRequest) {
        User user = userStorage.getUserById(id).orElseThrow(() -> {
            log.warn("Updating user failed: user with ID {} not found", id);
            return new UserNotFoundException("Error when updating user", id);
        });
        Set<ConstraintViolation<UserDto>> violations = validator.validate(
                updateUserRequest);
        if (!violations.isEmpty()) {
            log.warn("Updating user failed: {}", violations.iterator().next().getMessage());
            throw new UserValidationException("Error when updating user",
                    violations.iterator().next().getMessage());
        }
        if (!userStorage.getUserByEmail(updateUserRequest.getEmail()).isEmpty()) {
            log.debug("User had duplicated email");
            throw new UserEmailException("Error when updating user",
                    "User has duplicated email");
        }
        log.debug("Updating user with ID {}: {}", id, updateUserRequest);
        user = mapper.updateUserFields(user, updateUserRequest);
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        if (!userStorage.checkUserExists(userId)) {
            log.warn("Deleting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when deleting user", userId);
        }
        log.debug("Deleting user with ID {}", userId);
        userStorage.deleteUser(userId);
    }

}