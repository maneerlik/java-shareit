package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Репозиторий для работы с {@code Item}
 *
 * <p>Расширяет {@link JpaRepository}. Предоставляет стандартные CRUD-операции и
 * специализированные методы для поиска вещей</p>
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Поиск всех вещей по ID запроса на размещение вещи
     *
     * @param requestId ID запроса на размещение вещи
     * @return лист вещей
     */
    List<Item> findByRequestId(Long requestId);

    /**
     * Поиск всех вещей по ID владельца
     *
     * @param id ID владельца вещи
     * @return лист вещей
     */
    List<Item> findAllByOwnerId(Long id);

    /**
     * Поиск всех вещей по подстроке (шаблону) входящей
     * в имя {@code name} или описание {@code description} вещи
     *
     * @param text подстрока (шаблон поиска)
     * @return лист вещей
     */
    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', :text, '%')) " +
            "   or upper(i.description) like upper(concat('%', :text, '%'))")
    List<Item> search(@Param("text") String text);
}
