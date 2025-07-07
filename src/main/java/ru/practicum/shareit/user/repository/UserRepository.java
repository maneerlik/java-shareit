package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Репозиторий для работы с {@code User}
 *
 * <p>Расширяет {@link JpaRepository}. Предоставляет стандартные
 * CRUD-операции для сущности пользователь</p>
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
