package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {
    boolean checkUserExists(long userId);
    Optional<User> getUserById(long userId);
    Optional<User> getUserByEmail(String email);
    long addUser(UserDto userDto);
    void updateUser(User user);
    void deleteUser(long userId);
}
