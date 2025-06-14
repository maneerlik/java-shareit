package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * Класс {@code Item} - модель вещи
 *
 * <p>Аннотация {@code @Data} автоматически генерирует геттеры, сеттеры, методы {@code equals()},
 * {@code hashCode()}, а также {@code toString()}. Аннотация {@code @NoArgsConstructor}
 * автоматически генерирует конструктор по умолчанию, который необходим при
 * сериализации/десериализации объектов JSON</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор вещи</li>
 *   <li>{@code name} - название вещи</li>
 *   <li>{@code description} - описание вещи</li>
 *   <li>{@code available} - статус доступности для аренды</li>
 *   <li>{@code owner} - владелец вещи</li>
 *   <li>{@code request} - запрос по которому была размещена вещь</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
