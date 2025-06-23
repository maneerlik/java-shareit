package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@code User} - пользователь
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор вещи</li>
 *   <li>{@code name} - имя пользователя</li>
 *   <li>{@code email} - электронный адрес пользователя</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
