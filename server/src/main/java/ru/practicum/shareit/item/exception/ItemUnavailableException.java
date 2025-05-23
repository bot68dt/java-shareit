package ru.practicum.shareit.item.exception;

import lombok.Getter;
import ru.practicum.shareit.global.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;

@Getter
public class ItemUnavailableException extends BadRequestException {

    private final String message;
    private final String validationMessage;

    public ItemUnavailableException(String message, String validationMessage) {
        this.message = message;
        this.validationMessage = validationMessage;
    }

    public Class<?> getEntityType() {
        return Item.class;
    }
}