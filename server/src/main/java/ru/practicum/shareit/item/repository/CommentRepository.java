package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

/**
 * Репозиторий для работы с {@code Comment}
 *
 * <p>Расширяет {@link JpaRepository}. Предоставляет стандартные CRUD-операции и
 * специализированные методы для поиска отзывов</p>
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Поиск всех отзывов по ID вещи
     *
     * @param itemId ID вещи
     * @return коллекция отзывов
     */
    Collection<Comment> findByItemId(Long itemId);
}
