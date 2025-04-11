package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(long userId, BookingRequest newBookingRequest);

    BookingDto updateStatus(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    Collection<BookingDto> getBookingsByUserId(long userId, String state);

    Collection<BookingDto> getBookingsByOwnerId(long userId, String state);
}