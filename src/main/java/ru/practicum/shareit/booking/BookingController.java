package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;


    @PostMapping
    public BookingDtoResponse createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody BookingDto bookingDto
    ) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved
    ) {
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDtoResponse> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.getOwnerBookings(userId, state);
    }
}
