package ru.practicum.shareit.booking.exception;

import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingRequest;

@Getter
public class InvalidDateTimeException extends RuntimeException {

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