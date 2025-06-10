package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto, Long statusId) {
        Booking booking = new Booking();

        booking.setId(bookingDto.getId());
        booking.setItemId(bookingDto.getItemId());
        booking.setBookerId(bookingDto.getBookerId());
        booking.setStartDate(bookingDto.getStartDate());
        booking.setEndDate(bookingDto.getEndDate());
        booking.setStatusId(statusId);

        return booking;
    }

    public BookingDto toBookingDto(Booking booking, String statusName) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .statusName(statusName)
                .build();
    }
}
