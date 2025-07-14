package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Репозиторий для работы с {@code Booking}
 *
 * <p>Расширяет {@link JpaRepository}. Предоставляет стандартные CRUD-операции и
 * специализированные методы для поиска бронирований</p>
 */

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Поиск всех бронирований по ID бронирующего пользователя
     *
     * @param bookerId ID бронирующего пользователя
     * @param sort     параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByBookerId(Long bookerId, Sort sort);

    /**
     * Поиск всех бронирований по ID вещи
     *
     * @param itemId ID вещи
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemId(Long itemId);

    /**
     * Поиск всех бронирований по ID владельца вещи
     *
     * @param ownerId ID владельца вещи
     * @param sort    параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    /**
     * Поиск всех завершенных бронирований пользователя (где дата окончания раньше указанной)
     *
     * @param bookerId ID бронирующего пользователя
     * @param end      дата, до которой должно завершиться бронирование
     * @param sort     параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByBookerIdAndEndDateIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    /**
     * Поиск всех будущих бронирований пользователя (где дата начала позже указанной)
     *
     * @param bookerId ID бронирующего пользователя
     * @param start    дата, после которой должно начаться бронирование
     * @param sort     параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByBookerIdAndStartDateIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    /**
     * Поиск всех бронирований пользователя с указанным статусом
     *
     * @param bookerId ID бронирующего пользователя
     * @param status   статус бронирования
     * @param sort     параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    /**
     * Поиск всех завершенных бронирований по владельцу вещи (где дата окончания раньше указанной)
     *
     * @param ownerId ID владельца вещи
     * @param end     дата, до которой должно завершиться бронирование
     * @param sort    параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemOwnerIdAndEndDateIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    /**
     * Поиск всех будущих бронирований по владельцу вещи (где дата начала позже указанной)
     *
     * @param ownerId ID владельца вещи
     * @param start   дата, после которой должно начаться бронирование
     * @param sort    параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemOwnerIdAndStartDateIsAfter(Long ownerId, LocalDateTime start, Sort sort);

    /**
     * Поиск всех бронирований по владельцу вещи с указанным статусом
     *
     * @param ownerId ID владельца вещи
     * @param status  статус бронирования
     * @param sort    параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    /**
     * Поиск всех текущих бронирований пользователя (где текущая дата между началом и окончанием бронирования)
     *
     * @param bookerId ID бронирующего пользователя
     * @param start    дата, после которой должно начаться бронирование
     * @param end      дата, до которой должно завершиться бронирование
     * @param sort     параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByBookerIdAndStartDateIsBeforeAndEndDateIsAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort
    );

    /**
     * Поиск всех текущих бронирований по владельцу вещи (где текущая дата между началом и окончанием бронирования)
     *
     * @param ownerId ID владельца вещи
     * @param start   дата, после которой должно начаться бронирование
     * @param end     дата, до которой должно завершиться бронирование
     * @param sort    параметры сортировки результатов
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemOwnerIdAndStartDateIsBeforeAndEndDateIsAfter(
            Long ownerId, LocalDateTime start, LocalDateTime end, Sort sort
    );

    /**
     * Поиск завершенных бронирований конкретной вещи конкретным пользователем
     *
     * @param itemId   ID вещи
     * @param bookerId ID бронирующего пользователя
     * @param end      дата, до которой должно завершиться бронирование
     * @return коллекция бронирований
     */
    Collection<Booking> findByItemIdAndBookerIdAndEndDateIsBefore(
            Long itemId, Long bookerId, LocalDateTime end
    );
}
