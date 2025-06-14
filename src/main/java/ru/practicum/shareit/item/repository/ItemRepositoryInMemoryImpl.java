package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.function.Predicate;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long nextId = 1L;


    @Override
    public Item save(Item item) {
        item.setId(nextId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.nonNull(item.getOwner()))
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> findByPattern(String pattern) {
        if (pattern.trim().isEmpty()) return Collections.emptyList();

        String lowerCasePattern = pattern.toLowerCase();
        Predicate<Item> matcher = item ->
                item.getName().toLowerCase().contains(lowerCasePattern) ||
                item.getDescription().toLowerCase().contains(lowerCasePattern);

        return items.values().stream()
                .filter(item -> Objects.nonNull(item.getName()) && Objects.nonNull(item.getDescription()))
                .filter(Item::getAvailable)
                .filter(matcher)
                .toList();
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }
}
