package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс {@code ItemRequest} - запрос на размещение вещи
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор запроса</li>
 *   <li>{@code description} - текст запроса на размещение вещи</li>
 *   <li>{@code requestor} - пользователь разместивший запрос</li>
 *   <li>{@code created} - дата создания запроса</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
