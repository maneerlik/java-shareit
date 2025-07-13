package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    private AutoCloseable mocks;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

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
        Booking bookingOne = new Booking();
        bookingOne.setId(1L);

        Booking bookingTwo = new Booking();
        bookingTwo.setId(1L);

        Booking bookingThree = new Booking();
        bookingThree.setId(2L);

        assertEquals(bookingOne, bookingOne);

        assertEquals(bookingOne, bookingTwo);
        assertEquals(bookingTwo, bookingOne);

        Booking bookingFour = new Booking();
        bookingFour.setId(1L);
        assertEquals(bookingOne, bookingTwo);
        assertEquals(bookingTwo, bookingFour);
        assertEquals(bookingOne, bookingFour);

        assertEquals(bookingOne, bookingTwo);

        assertNotEquals(null, bookingOne);

        assertNotEquals(bookingOne, bookingThree);

        assertEquals(bookingOne.hashCode(), bookingTwo.hashCode());
    }

    @Test
    void createBooking_ValidBooking_ReturnsBookingDtoResponse() {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDtoResponse response = bookingService.createBooking(userId, bookingDto);

        assertNotNull(response);
        assertEquals(bookingDto.getStart(), response.getStart());
        assertEquals(bookingDto.getEnd(), response.getEnd());
        assertEquals(BookingStatus.WAITING, response.getStatus());
    }

    @Test
    void createBooking_ItemNotAvailable_ThrowsValidationException() {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(userId, bookingDto));
    }

    @Test
    void updateBookingStatus_ValidRequest_ReturnsBookingDtoResponse() {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;

        User notOwner = new User();
        notOwner.setId(2L);
        notOwner.setName("Not Owner");
        notOwner.setEmail("notowner@example.com");

        Item item = new Item();
        item.setId(1L);
        item.setOwner(notOwner);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);

        User booker = new User();
        booker.setId(3L);
        booker.setName("Booker");
        booker.setEmail("booker@example.com");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.updateBookingStatus(userId, bookingId, approved));
    }

    @Test
    void updateBookingStatus_NotOwner_ThrowsValidationException() {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;

        User notOwner = new User();
        notOwner.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(notOwner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.updateBookingStatus(userId, bookingId, approved));
    }

    @Test
    void getBooking_ValidRequest_ReturnsBookingDtoResponse() {
        Long userId = 1L;
        Long bookingId = 1L;

        User booker = new User();
        booker.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(booker);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDtoResponse response = bookingService.getBooking(userId, bookingId);

        assertNotNull(response);
        assertEquals(bookingId, response.getId());
    }

    @Test
    void getBooking_NotBookerOrOwner_ThrowsValidationException() {
        Long userId = 1L;
        Long bookingId = 1L;

        User notBookerOrOwner = new User();
        notBookerOrOwner.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(notBookerOrOwner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(notBookerOrOwner);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.getBooking(userId, bookingId));
    }

    @Test
    void getUserBookings_ValidRequest_ReturnsListOfBookingDtoResponse() {
        Long userId = 1L;
        String state = "ALL";

        User booker = new User();
        booker.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(booker);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findByBookerId(anyLong(), any())).thenReturn(Collections.singletonList(booking));

        Collection<BookingDtoResponse> responses = bookingService.getUserBookings(userId, state);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void getOwnerBookings_ValidRequest_ReturnsListOfBookingDtoResponse() {
        Long userId = 1L;
        String state = "ALL";

        User owner = new User();
        owner.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(owner);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerId(anyLong(), any())).thenReturn(Collections.singletonList(booking));

        Collection<BookingDtoResponse> responses = bookingService.getOwnerBookings(userId, state);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }
}
