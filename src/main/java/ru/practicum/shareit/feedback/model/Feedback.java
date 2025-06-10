package ru.practicum.shareit.feedback.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс {@code Feedback} - отзыв
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор отзыва</li>
 *   <li>{@code itemId} - идентификатор вещи на которую оставлен отзыв</li>
 *   <li>{@code userId} - автор отзыва</li>
 *   <li>{@code text} - текст отзыва</li>
 *   <li>{@code createdDate} - дата создания отзыва</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Feedback {
    private Long id;
    private Long itemId;
    private Long userId;
    private String text;
    private LocalDateTime createdDate;
}
