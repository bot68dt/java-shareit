package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestsMapperNew {
    /*public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    public static List<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(mapToBookingDto(booking));
        }

        return result;
    }*/

    public static ItemRequest mapToNewItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }
}
