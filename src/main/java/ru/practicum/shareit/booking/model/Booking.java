package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс {@code Booking} - модель бронирование
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор бронирования</li>
 *   <li>{@code itemId} - идентификатор бронируемой вещи</li>
 *   <li>{@code bookerId} - идентификатор бронирующего пользователя</li>
 *   <li>{@code startDate} - дата начала бронирования</li>
 *   <li>{@code endDate} - дата окончания бронирования</li>
 *   <li>{@code statusId} - идентификатор статуса брони</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Booking {
    private Long id;
    private Long itemId;
    private Long bookerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long statusId;
}
