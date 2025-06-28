package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс {@code Booking} - сущность бронирования в системе
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
 *   <li>{@code id} - уникальный идентификатор бронирования</li>
 *   <li>{@code startDate} - дата начала бронирования</li>
 *   <li>{@code endDate} - дата окончания бронирования</li>
 *   <li>{@code item} - бронируемая вещь</li>
 *   <li>{@code booker} - бронирующий пользователь</li>
 *   <li>{@code status} - статус брони</li>
 * </ul>
 */

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
