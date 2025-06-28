package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс {@code User} - сущность пользователя в системе
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
 *   <li>{@code id} - уникальный идентификатор вещи</li>
 *   <li>{@code name} - имя пользователя</li>
 *   <li>{@code email} - электронный адрес пользователя</li>
 * </ul>
 */

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
