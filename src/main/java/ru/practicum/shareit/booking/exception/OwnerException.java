package ru.practicum.shareit.booking.exception;

import lombok.Getter;
import ru.practicum.shareit.booking.model.Booking;

@Getter
public class OwnerException extends RuntimeException {

    private final String message;
    private final long id;

    public OwnerException(String message, long id) {
        this.message = message;
        this.id = id;
    }

    public Class<?> getEntityType() {
        return Booking.class;
    }
}