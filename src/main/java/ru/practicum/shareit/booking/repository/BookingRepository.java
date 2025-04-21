package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findDistinctByIdAndItemOwnerId(long bookingId, long ownerId);

    Optional<Booking> findDistinctById(long bookingId);

    @Query(" select b from Booking b where (b.id = ?1 and (b.item.owner.id = ?2 or b.booker.id = ?2)) ")
    Optional<Booking> findBookingByIdAndUser(long bookingId, long userId);

    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<Booking> findByBookerIdAndEndAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long userId);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<Booking> findByItemOwnerIdAndEndAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime end);

    Optional<Booking> findFirst1ByItemIdAndItemOwnerIdAndStartBeforeOrderByStartDesc(long itemId, long userId, LocalDateTime end);

    Optional<Booking> findFirst1ByItemIdAndItemOwnerIdAndStartAfterOrderByStart(long itemId, long userId, LocalDateTime end);

    Optional<Booking> findFirst1ByItemIdAndBookerIdAndEndBefore(long itemId, long userId, LocalDateTime end);
}