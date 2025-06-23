package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
 *   <li>{@code startDate} - дата начала бронирования</li>
 *   <li>{@code endDate} - дата окончания бронирования</li>
 *   <li>{@code item} - бронируемая вещь</li>
 *   <li>{@code booker} - бронирующий пользователь</li>
 *   <li>{@code status} - статус брони</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Booking {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Item item;
    private User booker;
    private BookingStatus status;
}
