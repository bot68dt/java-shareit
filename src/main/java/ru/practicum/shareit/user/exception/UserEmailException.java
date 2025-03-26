package ru.practicum.shareit.user.exception;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserEmailException extends RuntimeException {

    private final String message;
    private final String creationMessage;

    public UserEmailException(String message, String creationMessage) {
        this.message = message;
        this.creationMessage = creationMessage;
    }

    public Class<?> getEntityType() {
        return User.class;
    }
}