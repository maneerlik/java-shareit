package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.shareit.item.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final UserService userService;


    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = toUser(userService.getUser(userId));
        log.info("Creating item: {}", itemDto);
        Item item = toItem(itemDto);
        item.setOwner(user);
        Long requestId = itemDto.getRequestId();
        if (requestId != null) item.setRequest(requestRepository.findById(requestId).get());
        Item savedItem = itemRepository.save(item);
        log.info("Created item: {}", item);
        return toItemDto(savedItem);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        Collection<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndEndDateIsBefore(
                itemId, userId, LocalDateTime.now()
        );

        if (bookings.isEmpty()) throw new ValidationException("User has not booked this item");

        Comment comment = toComment(commentDto, item, user);
        Comment savedComment = commentRepository.save(comment);

        return toCommentDto(savedComment);
    }

    @Override
    public ItemDtoResponse getItem(Long userId, Long itemId) {
        log.info("Getting item with id: {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        if (!item.getOwner().getId().equals(userId)) {
            Collection<Booking> bookings = bookingRepository.findByItemId(item.getId());
            Collection<Comment> comments = commentRepository.findByItemId(item.getId());
            return toItemDtoResponse(item, bookings, comments);
        }

        log.info("Found item: {}", item);
        return toItemDtoResponseWithBookingsAndComments(item);
    }

    @Override
    public Collection<ItemDtoResponse> getUserItems(Long userId) {
        log.info("Getting items for user with id: {}", userId);
        List<Item> items = new ArrayList<>(itemRepository.findAllByOwnerId(userId));
        List<ItemDtoResponse> itemDtos = items.stream().map(this::toItemDtoResponseWithBookingsAndComments).toList();

        log.info("Found {} items for user with id: {}", items.size(), userId);
        return itemDtos;
    }

    @Override
    public Collection<ItemDtoResponse> getItemsByPattern(String pattern) {
        if (pattern.trim().isEmpty()) return Collections.emptyList();

        log.info("Searching items by pattern: {}", pattern);

        List<Item> items = itemRepository.search(pattern).stream()
                .filter(Item::getAvailable)
                .toList();

        List<ItemDtoResponse> itemDtos = items.stream().map(this::toItemDtoResponseWithBookingsAndComments).toList();

        log.info("Found {} items", items.size());
        return itemDtos;
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        log.info("Updating item with id: {} for user with id: {}", itemId, userId);
        Item updatingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        if (Objects.nonNull(itemDto.getName())) updatingItem.setName(itemDto.getName());
        if (Objects.nonNull(itemDto.getDescription())) updatingItem.setDescription(itemDto.getDescription());
        if (Objects.nonNull(itemDto.getAvailable())) updatingItem.setAvailable(itemDto.getAvailable());

        log.info("Updated item: {}", updatingItem);
        return toItemDto(updatingItem);
    }

    @Override


    public void deleteItem(Long itemId) {
        log.info("Deleting item with id: {}", itemId);
        itemRepository.deleteById(itemId);
        log.info("Item deleted: {}", itemId);
    }

    private ItemDtoResponse toItemDtoResponseWithBookingsAndComments(Item item) {
        Collection<Booking> bookings = bookingRepository.findByItemId(item.getId());
        Collection<Comment> comments = commentRepository.findByItemId(item.getId());

        LocalDateTime lastBookingDate = bookings.stream()
                .map(Booking::getEndDate)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime nextBookingDate = bookings.stream()
                .map(Booking::getStartDate)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        ItemDtoResponse itemDtoResponse = toItemDtoResponse(item, bookings, comments);
        itemDtoResponse.setLastBooking(lastBookingDate);
        itemDtoResponse.setNextBooking(nextBookingDate);
        return itemDtoResponse;
    }
}
