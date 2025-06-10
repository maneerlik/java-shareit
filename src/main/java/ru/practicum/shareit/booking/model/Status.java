package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@code Status} - статус бронирования
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор статуса</li>
 *   <li>{@code name} - статус</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Status {
    private Long id;
    private String name;
}
