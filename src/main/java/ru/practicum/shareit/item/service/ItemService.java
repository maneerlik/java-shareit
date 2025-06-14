package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    public ItemDto getItem(Long itemId);

    public Collection<ItemDto> getUserItems(Long userId);

    public Collection<ItemDto> getItemsByPattern(String pattern);

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    public void deleteItem(Long itemId);
}
