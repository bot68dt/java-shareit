package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserMapper {

    UserDto mapToUserDto(User user);

    User mapToUserModel(UserDto newUserRequest);

    User updateUserFields(User user, UserDto updateUserRequest);
}