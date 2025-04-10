package ru.practicum.shareit.user.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserCreationException;
import ru.practicum.shareit.user.exception.UserEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.mapper.UserMapperNew;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public UserDto getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", id);
            throw new UserNotFoundException("Error when getting user", id);
        }
        log.debug("Getting user with ID {}", id);
        return UserMapperNew.mapToUserDto(user.get());
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto newUserRequest) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(newUserRequest);
        if (!violations.isEmpty()) {
            log.warn("Adding user failed: {}", violations.iterator().next().getMessage());
            throw new UserValidationException("Error when creating new user", violations.iterator().next().getMessage());
        }
        if (newUserRequest.getName() == null) {
            log.debug("User had no name");
            throw new UserCreationException("Error when creating new user", "User has no name");
        }
        if (newUserRequest.getEmail() == null) {
            log.debug("User had no email");
            throw new UserCreationException("Error when creating new user", "User has no email");
        }
        if (userRepository.findByEmail(newUserRequest.getEmail()).isPresent()) {
            log.debug("User had duplicated email");
            throw new UserEmailException("Error when updating user", "User has duplicated email");
        }
        User user = userRepository.saveAndFlush(UserMapperNew.mapToNewUser(newUserRequest));
        log.debug("Adding new user {}", newUserRequest);
        return UserMapperNew.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long id, UserDto updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Updating user failed: user with ID {} not found", id);
            return new UserNotFoundException("Error when updating user", id);
        });
        Set<ConstraintViolation<UserDto>> violations = validator.validate(updateUserRequest);
        if (!violations.isEmpty()) {
            log.warn("Updating user failed: {}", violations.iterator().next().getMessage());
            throw new UserValidationException("Error when updating user", violations.iterator().next().getMessage());
        }
        if (userRepository.findByEmail(updateUserRequest.getEmail()).isPresent()) {
            log.debug("User had duplicated email");
            throw new UserEmailException("Error when updating user", "User has duplicated email");
        }
        log.debug("Updating user with ID {}: {}", id, updateUserRequest);
        UserMapperNew.updateUserFields(user, updateUserRequest);
        userRepository.saveAndFlush(user);
        return UserMapperNew.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (!userRepository.existsUserById(userId)) {
            log.warn("Deleting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when deleting user", userId);
        }
        log.debug("Deleting user with ID {}", userId);
        userRepository.deleteById(userId);
    }

}