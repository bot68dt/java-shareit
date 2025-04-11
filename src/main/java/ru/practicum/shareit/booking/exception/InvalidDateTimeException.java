package ru.practicum.shareit.booking.exception;

import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.global.exception.BadRequestException;

@Getter
public class InvalidDateTimeException extends BadRequestException {

    private final String message;
    private final String validationMessage;

    public InvalidDateTimeException(String message, String validationMessage) {
        this.message = message;
        this.validationMessage = validationMessage;
    }

    public Class<?> getEntityType() {
        return BookingRequest.class;
    }
}