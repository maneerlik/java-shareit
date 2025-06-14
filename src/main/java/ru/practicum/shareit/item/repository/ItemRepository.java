package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(Long itemId);

    Collection<Item> findByUserId(Long userId);

    Collection<Item> findByPattern(String pattern);

    Item update(Item item);

    void deleteById(Long itemId);
}
