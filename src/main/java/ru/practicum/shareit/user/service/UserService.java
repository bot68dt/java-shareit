package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    User getUserById(long id);
    User addUser(UserDto userDto);
    User updateUser(long id, UserDto userDto);
    void deleteUser(long id);
}