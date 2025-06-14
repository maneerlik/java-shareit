package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Objects;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        if (Objects.nonNull(item.getRequest())) itemDto.setRequestId(item.getRequest().getId());
        return itemDto;
    }
}
