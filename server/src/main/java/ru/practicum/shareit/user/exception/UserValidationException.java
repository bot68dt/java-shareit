package ru.practicum.shareit.user.exception;

import lombok.Getter;
import ru.practicum.shareit.global.exception.BadRequestException;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserValidationException extends BadRequestException {

    private final String message;
    private final String validationMessage;

    public UserValidationException(String message, String validationMessage) {
        this.message = message;
        this.validationMessage = validationMessage;
    }

    public Class<?> getEntityType() {
        return User.class;
    }
}