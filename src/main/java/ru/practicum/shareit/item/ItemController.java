package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody ItemDto itemDto
    ) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ArrayList<>(itemService.getUserItems(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByPattern(@RequestParam("text") String pattern) {
        return new ArrayList<>(itemService.getItemsByPattern(pattern));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
