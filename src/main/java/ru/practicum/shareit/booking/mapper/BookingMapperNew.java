package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapperNew {
    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    public static List<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(mapToBookingDto(booking));
        }

        return result;
    }

    public static Booking updateBookingState(Booking booking, BookingDto bookingDto) {
        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }
        return booking;
    }
}