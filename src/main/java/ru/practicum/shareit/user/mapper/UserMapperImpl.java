package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

    @Override
    public User mapToUserModel(UserDto newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

    @Override
    public User updateUserFields(User user, UserDto updateUserRequest) {
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }
        return user;
    }
}