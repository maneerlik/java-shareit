package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс {@code Comment} - сущность отзыва в системе
 *
 * <p>Аннотации {@code @Getter}, {@code @Setter} и {@code @ToString} автоматически генерируют
 * соответствующие методы. Аннотация {@code @NoArgsConstructor} создает конструктор по умолчанию,
 * необходимый для JPA и сериализации/десериализации объектов JSON</p>
 *
 * <p>Аннотацию {@code @Column} можно не указывать, если имя поля в сущности совпадает с именем
 * столбца в таблице (прямо или с учетом правил именования JPA, например, camelCase -> snake_case),
 * а также если не требуется дополнительная конфигурация (nullable, unique и т.д.). {@code @JoinColumn}
 * (для связей) тоже можно опускать, если имя столбца формируется по умолчанию (например, booker -> booker_id)</p>
 *
 * <p>Поля класса:</p>
 * <ul>
 *   <li>{@code id} - уникальный идентификатор отзыва</li>
 *   <li>{@code text} - текст отзыва</li>
 *   <li>{@code item} - вещь на которую оставлен отзыв</li>
 *   <li>{@code author} - автор отзыва</li>
 *   <li>{@code created} - дата создания комментария</li>
 * </ul>
 */

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User author;

    @Column(name = "created_at")
    private LocalDateTime created;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
