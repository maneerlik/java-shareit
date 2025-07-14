package ru.practicum.shareit.booking.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Перечисление {@code BookingState} - состояние бронирования
 *
 * <p>Перечисление содержит константы для состояний бронирования, которое может принимать
 * обязательный параметр {@code state} метода - "получение списка всех бронирований текущего
 * пользователя" по эндпоинтам {@code GET: /bookings} или {@code GET: /bookings/owner}</p>
 *
 * <p>Константы перечисления:</p>
 * <ul>
 *   <li>{@code ALL} - все (значение параметра по умолчанию)</li>
 *   <li>{@code CURRENT} - текущие</li>
 *   <li>{@code PAST} - завершенные</li>
 *   <li>{@code FUTURE} - будущие</li>
 *   <li>{@code REJECTED} - ожидающие подтверждения</li>
 *   <li>{@code WAITING} - отклонённые</li>
 * </ul>
 */

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> from(String stringState) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
