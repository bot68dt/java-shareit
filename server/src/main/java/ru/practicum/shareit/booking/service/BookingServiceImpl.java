package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.InvalidDateTimeException;
import ru.practicum.shareit.booking.exception.OwnerException;
import ru.practicum.shareit.booking.mapper.BookingMapperNew;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemUnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.InternalServerException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public BookingDto createBooking(long userId, BookingRequest newBookingRequest) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Creating booking failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when creating booking. User not found", userId);
        }

        Optional<Item> item = itemRepository.findDistinctById(newBookingRequest.getItemId());
        if (item.isEmpty()) {
            log.warn("Creating booking failed: item with ID {} not found", newBookingRequest.getItemId());
            throw new ItemNotFoundException("Error when creating booking. Item not found", newBookingRequest.getItemId());
        }
        if (!item.get().getAvailable()) {
            log.warn("Creating booking failed: item with ID {} is not available", newBookingRequest.getItemId());
            throw new ItemUnavailableException("Error when creating booking. Item is not available", newBookingRequest.getItemId().toString());
        }
        if (item.get().getOwner().getId().equals(userId)) {
            log.warn("Creating booking failed: user with ID {} is the owner of the item", userId);
            throw new UserNotFoundException("Error when creating booking. User is the owner", userId);
        }
        if (newBookingRequest.getEnd().isEqual(newBookingRequest.getStart()) || newBookingRequest.getEnd().isBefore(newBookingRequest.getStart())) {
            log.warn("Creating booking failed: fields with DateTime in request {} is incorrect", newBookingRequest);
            throw new InvalidDateTimeException("Error when creating booking. Fields with DateTime in request is incorrect", newBookingRequest.toString());
        }

        Booking booking = new Booking();
        booking.setBooker(user.get());
        booking.setItem(item.get());
        booking.setStart(newBookingRequest.getStart());
        booking.setEnd(newBookingRequest.getEnd());
        booking.setStatus(Status.valueOf("WAITING"));
        log.debug("Adding new item {}", newBookingRequest);
        return BookingMapperNew.mapToBookingDto(bookingRepository.saveAndFlush(booking));
    }

    @Override
    public BookingDto updateStatus(long userId, long bookingId, boolean approved) {

        Optional<Booking> booking = bookingRepository.findDistinctByIdAndItemOwnerId(bookingId, userId);
        if (booking.isEmpty()) {
            log.warn("Updating booking failed: booking with ID {} can not be updated", bookingId);
            Optional<Booking> check = bookingRepository.findDistinctById(bookingId);
            if (check.isEmpty()) {
                log.warn("Fail reason: booking with ID {} not found", bookingId);
                throw new BookingNotFoundException("Error when creating booking. Booking not found", bookingId);
            }
            if (!check.get().getItem().getOwner().getId().equals(userId)) {
                log.warn("Fail reason: User with ID {} is not an owner", userId);
                throw new OwnerException("Error when updating booking. User is not an owner", userId);
            }
        }
        Booking newBooking = booking.get();
        if (approved) {
            newBooking.setStatus(Status.APPROVED);
        } else {
            newBooking.setStatus(Status.REJECTED);
        }
        bookingRepository.saveAndFlush(newBooking);
        return BookingMapperNew.mapToBookingDto(newBooking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findBookingByIdAndUser(bookingId, userId);
        if (booking.isEmpty()) {
            log.warn("Updating booking failed: booking with ID {} can not be found", bookingId);
            Optional<Booking> check = bookingRepository.findDistinctById(bookingId);
            if (check.isEmpty()) {
                log.warn("Fail reason: booking with ID {} not found", bookingId);
                throw new BookingNotFoundException("Error when searching booking. Booking not found", bookingId);
            }
            if (!check.get().getItem().getOwner().getId().equals(userId)) {
                log.warn("Fail reason: User with ID {} is not an owner or a booker", userId);
                throw new OwnerException("Error when updating booking. User is not an owner of a booker", userId);
            }
        }
        return BookingMapperNew.mapToBookingDto(booking.get());
    }

    @Override
    public Collection<BookingDto> getBookingsByUserId(long userId, String state) {

        List<Booking> bookings = switch (state) {
            case "CURRENT" -> bookingRepository.findByBookerIdAndEndAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "PAST" -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case "FUTURE" -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "WAITING" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case "ALL" -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
            default -> throw new InternalServerException("Invalid state string");
        };

        return BookingMapperNew.mapToBookingDto(bookings);
    }

    @Override
    public Collection<BookingDto> getBookingsByOwnerId(long userId, String state) {

        Optional<Item> item = itemRepository.findDistinctById(userId);
        if (item.isEmpty()) {
            log.warn("Getting bookings failed. User with ID {} is not an owner of any item", userId);
            throw new UserNotFoundException("Error when getting booking. User is not an owner of any item", userId);
        }
        List<Booking> bookings = switch (state) {
            case "CURRENT" ->
                    bookingRepository.findByItemOwnerIdAndEndAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "PAST" -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case "FUTURE" ->
                    bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "WAITING" -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED" -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case "ALL" -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            default -> throw new InternalServerException("Invalid state string");
        };

        return BookingMapperNew.mapToBookingDto(bookings);
    }
}