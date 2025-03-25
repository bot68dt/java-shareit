package ru.practicum.shareit.item.exception;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class ItemNotFoundException extends RuntimeException {

    private final String message;
    private final long id;

    public ItemNotFoundException (String message, long id) {
        this.message = message;
        this.id = id;
    }

    public Class<?> getEntityType() {
        return User.class;
    }
}