package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class ItemServiceTest {
    private AutoCloseable mocks;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }


    @Test
    void testEqualsAndHashCode_CommentEntity() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertEquals(comment1, comment1);

        assertEquals(comment1, comment2);
        assertEquals(comment2, comment1);

        Comment comment4 = new Comment();
        comment4.setId(1L);
        assertEquals(comment1, comment2);
        assertEquals(comment2, comment4);
        assertEquals(comment1, comment4);

        assertEquals(comment1, comment2);

        assertNotEquals(null, comment1);

        assertNotEquals(comment1, comment3);

        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_ItemEntity() {
        Item Item1 = new Item();
        Item1.setId(1L);

        Item Item2 = new Item();
        Item2.setId(1L);

        Item Item3 = new Item();
        Item3.setId(2L);

        assertEquals(Item1, Item1);

        assertEquals(Item1, Item2);
        assertEquals(Item2, Item1);

        Item Item4 = new Item();
        Item4.setId(1L);
        assertEquals(Item1, Item2);
        assertEquals(Item2, Item4);
        assertEquals(Item1, Item4);

        assertEquals(Item1, Item2);

        assertNotEquals(null, Item1);

        assertNotEquals(Item1, Item3);

        assertEquals(Item1.hashCode(), Item2.hashCode());
    }
    
    @Test
    void createItem_ValidItem_ReturnsItemDto() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(UserMapper.toUser(userDto));

        when(userService.getUser(userId)).thenReturn(userDto);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.createItem(userId, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    void addComment_ValidComment_ReturnsCommentDto() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        Item item = new Item();
        item.setId(itemId);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStartDate(LocalDateTime.now().minusDays(2));
        booking.setEndDate(LocalDateTime.now().minusDays(1));

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndBookerIdAndEndDateIsBefore(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addComment(userId, itemId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());
    }

    @Test
    void addComment_UserHasNotBookedItem_ThrowsValidationException() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        Item item = new Item();
        item.setId(itemId);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndBookerIdAndEndDateIsBefore(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
    }

    @Test
    void getItem_ValidItemId_ReturnsItemDtoResponse() {
        Long userId = 1L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        Item item = new Item();
        item.setId(itemId);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemId(itemId)).thenReturn(Collections.emptyList());
        when(commentRepository.findByItemId(itemId)).thenReturn(Collections.emptyList());

        ItemDtoResponse result = itemService.getItem(userId, itemId);

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void getUserItems_ValidUserId_ReturnsListOfItemDtoResponse() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        when(itemRepository.findAllByOwnerId(userId)).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());

        List<ItemDtoResponse> result = (List<ItemDtoResponse>) itemService.getUserItems(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getName(), result.get(0).getName());
    }

    @Test
    void getItemsByPattern_ValidPattern_ReturnsListOfItemDtoResponse() {
        String pattern = "Item";

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);

        when(itemRepository.search(pattern)).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());

        List<ItemDtoResponse> result = (List<ItemDtoResponse>) itemService.getItemsByPattern(pattern);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getName(), result.get(0).getName());
    }

    @Test
    void updateItem_ValidItem_ReturnsItemDto() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");
        itemDto.setAvailable(false);

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        Item item = new Item();
        item.setId(itemId);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto result = itemService.updateItem(userId, itemId, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    void deleteItem_ValidItemId_DeletesItem() {
        Long itemId = 1L;
        doNothing().when(itemRepository).deleteById(itemId);
        assertDoesNotThrow(() -> itemService.deleteItem(itemId));
    }
}
