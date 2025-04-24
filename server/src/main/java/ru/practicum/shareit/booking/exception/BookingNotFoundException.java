package ru.practicum.shareit.booking.exception;

import lombok.Getter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.global.exception.NotFoundException;

@Getter
public class BookingNotFoundException extends NotFoundException {

    private final String message;
    private final long id;

    public BookingNotFoundException(String message, long id) {
        this.message = message;
        this.id = id;
    }

    public Class<?> getEntityType() {
        return Booking.class;
    }
}