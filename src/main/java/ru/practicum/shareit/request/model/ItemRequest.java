package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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
 *   <li>{@code userId} - пользователь разместивший запрос</li>
 *   <li>{@code description} - текст запроса на размещение вещи</li>
 *   <li>{@code createdDate} - дата создания запроса</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private Long userId;
    private String description;;
    private LocalDateTime createdDate;
}
