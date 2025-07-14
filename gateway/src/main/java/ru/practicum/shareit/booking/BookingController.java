package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

/**
 * Контроллер для обработки HTTP-запросов, связанных с бронированием.
 * Обеспечивает взаимодействие между клиентом и сервером. Все методы требуют
 * заголовок "X-Sharer-User-Id" для идентификации пользователя
 */

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;


    /**
     * Создать новое бронирование
     *
     * @param userId идентификатор пользователя (из заголовка X-Sharer-User-Id)
     * @param bookingDto DTO с данными для создания бронирования
     * @return ResponseEntity с результатом
     */
    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody BookingDto bookingDto
    ) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    /**
     * Получить информацию о конкретном бронировании
     *
     * @param userId идентификатор пользователя (из заголовка X-Sharer-User-Id)
     * @param bookingId идентификатор бронирования
     * @return ResponseEntity с результатом
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Получить список бронирований пользователя с фильтрацией по состоянию
     *
     * @param userId идентификатор пользователя (из заголовка X-Sharer-User-Id)
     * @param bookingState состояние бронирования для фильтрации (по умолчанию "ALL")
     * @return ResponseEntity со списком бронирований пользователя
     * @throws IllegalArgumentException если передано неизвестное состояние бронирования
     */
    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String bookingState
    ) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));
        log.info("Getting bookings with status {} for user {}", bookingState, userId);
        return bookingClient.getUserBookings(userId, state);
    }

    /**
     * Получить список бронирований вещей, принадлежащих пользователю, с возможностью фильтрации по состоянию
     *
     * @param ownerId идентификатор владельца вещей (из заголовка X-Sharer-User-Id)
     * @param bookingState состояние бронирования для фильтрации (по умолчанию "ALL")
     * @return ResponseEntity со списком бронирований вещей владельца
     * @throws IllegalArgumentException если передано неизвестное состояние бронирования
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "state", defaultValue = "ALL") String bookingState
    ) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));
        log.info("Getting bookings with status {} for owner item {}", bookingState, ownerId);
        return bookingClient.getOwnerBookings(ownerId, state);
    }

    /**
     * Обновить статус бронирования (подтверждение/отклонение)
     *
     * @param userId идентификатор пользователя (из заголовка X-Sharer-User-Id)
     * @param bookingId идентификатор бронирования
     * @param approved флаг подтверждения (true - подтвердить, false - отклонить)
     * @return ResponseEntity с обновленной информацией о бронировании
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }
}
