package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingRequest bookingDto) {
        log.info("Request to create new booking received: {}", bookingDto);
        BookingDto createdBooking = bookingService.createBooking(userId, bookingDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBooking.getId()).toUri();
        log.info("New booking created created with ID {}", createdBooking.getId());
        return ResponseEntity.created(location).body(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long bookingId, @RequestParam(value = "approved") boolean approved) {
        log.info("Request to update booking status received: {}", bookingId);
        BookingDto updatedBooking = bookingService.updateStatus(userId, bookingId, approved);
        log.info("Booking with ID {} updated", bookingId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable long bookingId) {
        log.info("Request to get booking with ID {} received.", bookingId);
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(required = false, defaultValue = "ALL", value = "state") String state) {
        log.info("Request to get all bookings of the user with ID {} received.", userId);
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> getAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                         @RequestParam(required = false, defaultValue = "ALL", value = "state") String state) {
        log.info("Request to get all bookings of the owner with ID {} received.", userId);
        return ResponseEntity.ok(bookingService.getBookingsByOwnerId(userId, state));
    }
}