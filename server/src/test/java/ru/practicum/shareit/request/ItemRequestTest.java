package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ItemRequestTest {
    private AutoCloseable mocks;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }


    @Test
    void testEqualsAndHashCode() {
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(1L);

        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setId(2L);

        assertEquals(itemRequest1, itemRequest1);

        assertEquals(itemRequest1, itemRequest2);
        assertEquals(itemRequest2, itemRequest1);

        ItemRequest itemRequest4 = new ItemRequest();
        itemRequest4.setId(1L);
        assertEquals(itemRequest1, itemRequest2);
        assertEquals(itemRequest2, itemRequest4);
        assertEquals(itemRequest1, itemRequest4);

        assertEquals(itemRequest1, itemRequest2);

        assertNotEquals(null, itemRequest1);

        assertNotEquals(itemRequest1, itemRequest3);

        assertEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }

    @Test
    void createRequest_ValidRequest_ReturnsItemRequestDto() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Request Description");

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request Description");
        itemRequest.setCreated(LocalDateTime.now());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.createRequest(userId, itemRequestDto);

        assertNotNull(result);
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
    }

    @Test
    void getUserRequests_ValidUserId_ReturnsListOfItemRequestDto() {
        Long userId = 1L;

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request Description");
        itemRequest.setCreated(LocalDateTime.now());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findByRequestorId(eq(userId), any(Sort.class)))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestDto> result = itemRequestService.getUserRequests(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.getFirst().getDescription());
    }

    @Test
    void getAllRequests_ValidRequest_ReturnsListOfItemRequestDto() {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request Description");
        itemRequest.setCreated(LocalDateTime.now());

        Page<ItemRequest> page = new PageImpl<>(Collections.singletonList(itemRequest));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<ItemRequestDto> result = itemRequestService.getAllRequests(userId, from, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.getFirst().getDescription());
    }

    @Test
    void getRequestById_ValidRequestId_ReturnsItemRequestDto() {
        Long userId = 1L;
        Long requestId = 1L;

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("Request Description");
        itemRequest.setCreated(LocalDateTime.now());

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(requestId)).thenReturn(Collections.singletonList(item));

        ItemRequestDto result = itemRequestService.getRequestById(userId, requestId);

        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(1, result.getItems().size());
    }
}
