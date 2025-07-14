package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@code ItemRequest} - сущность запроса на размещение вещи в системе
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
 *   <li>{@code id} - уникальный идентификатор запроса</li>
 *   <li>{@code description} - текст запроса на размещение вещи</li>
 *   <li>{@code requestor} - пользователь разместивший запрос</li>
 *   <li>{@code created} - дата создания запроса</li>
 *   <li>{@code items} - вещи которые были созданы в ответ на запрос</li>
 * </ul>
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requestor;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime created;

    @OneToMany
    @JoinColumn(name = "request_id")
    private List<Item> items = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequest)) return false;
        return id != null && id.equals(((ItemRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
