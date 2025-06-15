package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;


    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = toUser(userService.getUser(userId));
        log.info("Creating item: {}", itemDto);
        Item item = toItem(itemDto);
        item.setOwner(user);
        Item savedItem = itemRepository.save(item);
        log.info("Created item: {}", item);
        return toItemDto(savedItem);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        log.info("Getting item with id: {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));
        log.info("Found item: {}", item);
        return toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getUserItems(Long userId) {
        log.info("Getting items for user with id: {}", userId);
        List<Item> items = new ArrayList<>(itemRepository.findByUserId(userId));
        log.info("Found {} items for user with id: {}", items.size(), userId);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsByPattern(String pattern) {
        log.info("Searching items by pattern: {}", pattern);
        List<Item> items = new ArrayList<>(itemRepository.findByPattern(pattern));
        log.info("Found {} items", items.size());
        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.info("Updating item with id: {} for user with id: {}", itemId, userId);
        userService.getUser(userId);
        itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        Item updatingItem = new Item();
        updatingItem.setId(itemId);

        if (Objects.nonNull(itemDto.getName())) updatingItem.setName(itemDto.getName());
        if (Objects.nonNull(itemDto.getDescription())) updatingItem.setDescription(itemDto.getDescription());
        if (Objects.nonNull(itemDto.getAvailable())) updatingItem.setAvailable(itemDto.getAvailable());

        Item updatedItem = itemRepository.update(updatingItem);
        log.info("Updated item: {}", updatedItem);
        return toItemDto(updatedItem);
    }

    @Override
    public void deleteItem(Long itemId) {
        log.info("Deleting item with id: {}", itemId);
        itemRepository.deleteById(itemId);
        log.info("Item deleted: {}", itemId);
    }
}
