package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    ItemDtoResponse getItem(Long userId, Long itemId);

    Collection<ItemDtoResponse> getUserItems(Long userId);

    Collection<ItemDtoResponse> getItemsByPattern(String pattern);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);
}
