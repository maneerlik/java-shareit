package ru.practicum.shareit.booking.model;

/**
 * Перечисление {@code BookingStatus} - статус бронирования
 *
 * <p>Перечисление содержит константы для статусов бронирования, которые может принимать
 * сущность бронирования вещи. Статусы соответствуют значениям, принимаемым БД в таблице
 * {@code bookings} в соответствующем поле {@code status}</p>
 *
 * <p>Константы перечисления:</p>
 * <ul>
 *   <li>{@code WAITING} - новое бронирование, ожидает одобрения</li>
 *   <li>{@code APPROVED} - бронирование подтверждено владельцем</li>
 *   <li>{@code REJECTED} - бронирование отклонено владельцем</li>
 *   <li>{@code CANCELED} - бронирование отменено создателем</li>
 * </ul>
 */

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
