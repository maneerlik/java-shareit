package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBookerId(Long bookerId, Sort sort);

    Collection<Booking> findByItemId(Long itemId);

    Collection<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    Collection<Booking> findByBookerIdAndEndDateIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    Collection<Booking> findByBookerIdAndStartDateIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    Collection<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Collection<Booking> findByItemOwnerIdAndEndDateIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    Collection<Booking> findByItemOwnerIdAndStartDateIsAfter(Long ownerId, LocalDateTime start, Sort sort);

    Collection<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    Collection<Booking> findByBookerIdAndStartDateIsBeforeAndEndDateIsAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort
    );

    Collection<Booking> findByItemOwnerIdAndStartDateIsBeforeAndEndDateIsAfter(
            Long ownerId, LocalDateTime start, LocalDateTime end, Sort sort
    );

    Collection<Booking> findByItemIdAndBookerIdAndEndDateIsBefore(
            Long itemId, Long bookerId, LocalDateTime end
    );
}
