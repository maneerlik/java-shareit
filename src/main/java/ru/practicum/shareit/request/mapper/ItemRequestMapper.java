package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setUserId(itemRequestDto.getUserId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreatedDate(itemRequestDto.getCreatedDate());

        return itemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .userId(itemRequest.getUserId())
                .description(itemRequest.getDescription())
                .createdDate(itemRequest.getCreatedDate())
                .build();
    }
}
