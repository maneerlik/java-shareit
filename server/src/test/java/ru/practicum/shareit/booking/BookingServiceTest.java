package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    public static final Long USER_ID = 1L;
    public static final Long OTHER_USER_ID = 2L;
    public static final Long BOOKING_ID = 1L;
    public static final Long ITEM_ID = 1L;
    public static final String DEFAULT_STATE = "ALL";

    private AutoCloseable mocks;

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ItemRepository itemRepository;

    @InjectMocks private BookingServiceImpl bookingService;

    private User user;
    private User otherUser;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
       mocks = MockitoAnnotations.openMocks(this);

       user = new User();
       user.setId(USER_ID);

       otherUser = new User();
       otherUser.setId(OTHER_USER_ID);

       item = new Item();
       item.setId(ITEM_ID);
       item.setAvailable(Boolean.TRUE);

       booking = new Booking();
       booking.setId(BOOKING_ID);
       booking.setItem(item);
       booking.setBooker(user);
       booking.setStatus(BookingStatus.WAITING);

       bookingDto = new BookingDto();
       bookingDto.setItemId(ITEM_ID);
       bookingDto.setStart(LocalDateTime.now().plusDays(1));
       bookingDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }


    @Test
    void testEqualsAndHashCode() {
        Booking bookingOne = new Booking();
        bookingOne.setId(BOOKING_ID);

        Booking bookingTwo = new Booking();
        bookingTwo.setId(BOOKING_ID);

        Booking bookingThree = new Booking();
        bookingThree.setId(2L);

        assertAll(
                () -> assertEquals(bookingOne, bookingOne, "Рефлексивность не выполняется"),
                () -> assertEquals(bookingOne, bookingTwo, "Симметричность не выполняется (O1 != O2)"),
                () -> assertEquals(bookingTwo, bookingOne, "Симметричность не выполняется (O2 != O1)"),
                () -> assertEquals(bookingOne.hashCode(), bookingTwo.hashCode(), "hashCode не совпадает"),
                () -> assertNotEquals(bookingOne, bookingThree, "Неравенство не выполняется"),
                () -> assertNotEquals(null, bookingOne, "Сравнение с null не работает")
        );
    }

    @Test
    void createBooking_WhenValid_ShouldReturnBookingDtoResponse() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDtoResponse response = bookingService.createBooking(USER_ID, bookingDto);

        assertThat(response)
                .isNotNull()
                .satisfies(resp -> {
                    assertThat(resp.getStart()).isEqualTo(bookingDto.getStart());
                    assertThat(resp.getEnd()).isEqualTo(bookingDto.getEnd());
                    assertThat(resp.getStatus()).isEqualTo(BookingStatus.WAITING);
                });

    }

    @Test
    void createBooking_WhenItemNotAvailable_ShouldThrowValidationException() {
        item.setAvailable(Boolean.FALSE);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(USER_ID, bookingDto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void updateBookingStatus_WhenOwnerApproves_ShouldUpdateStatus() {
        item.setOwner(user);
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDtoResponse response = bookingService.updateBookingStatus(USER_ID, BOOKING_ID, Boolean.TRUE);

        assertThat(response)
                .isNotNull()
                .extracting(
                        BookingDtoResponse::getId,
                        BookingDtoResponse::getStatus)
                .containsExactly(
                        BOOKING_ID,
                        BookingStatus.APPROVED);
    }

    @Test
    void updateBookingStatus_WhenOwnerRejects_ShouldUpdateStatusToRejected() {
        item.setOwner(user);
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDtoResponse response = bookingService.updateBookingStatus(USER_ID, BOOKING_ID, Boolean.FALSE);

        assertThat(response)
                .isNotNull()
                .extracting(
                        BookingDtoResponse::getId,
                        BookingDtoResponse::getStatus)
                .containsExactly(
                        BOOKING_ID,
                        BookingStatus.REJECTED);
    }

    @Test
    void updateBookingStatus_WhenNotOwner_ShouldThrowValidationException() {
        item.setOwner(otherUser);
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.updateBookingStatus(USER_ID, BOOKING_ID, true))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Only owner can update item booking status");
    }

    @Test
    void getBooking_WhenValidRequest_ShouldReturnBookingDtoResponse() {
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        BookingDtoResponse response = bookingService.getBooking(USER_ID, BOOKING_ID);

        assertThat(response)
                .isNotNull()
                .extracting(BookingDtoResponse::getId)
                .isEqualTo(BOOKING_ID);
    }

    @Test
    void getBooking_WhenNotBookerOrOwner_ShouldThrowValidationException() {
        booking.setBooker(otherUser);
        item.setOwner(otherUser);
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBooking(USER_ID, BOOKING_ID))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Only booker or owner item can view the booking");
    }

    @Test
    void getUserBookings_WhenValidRequest_ShouldReturnListOfBookings() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(bookingRepository.findByBookerId(anyLong(), any())).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> responses = bookingService.getUserBookings(USER_ID, DEFAULT_STATE);

        assertThat(responses)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(BookingDtoResponse::getId)
                .isEqualTo(BOOKING_ID);
    }

    @Test
    void getOwnerBookings_WhenValidRequest_ShouldReturnListOfBookings() {
        when(userRepository.existsById(USER_ID)).thenReturn(Boolean.TRUE);
        when(bookingRepository.findByItemOwnerId(anyLong(), any())).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> responses = bookingService.getOwnerBookings(USER_ID, DEFAULT_STATE);

        assertThat(responses)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(BookingDtoResponse::getId)
                .isEqualTo(BOOKING_ID);
    }

    @Test
    void toBooking_ShouldMapCorrectly() {
        Booking result = BookingMapper.toBooking(user, item, bookingDto);

        assertThat(result)
                .isNotNull()
                .extracting(
                        Booking::getStartDate,
                        Booking::getEndDate,
                        Booking::getBooker,
                        Booking::getItem,
                        Booking::getStatus)
                .containsExactly(
                        bookingDto.getStart(),
                        bookingDto.getEnd(),
                        user,
                        item,
                        BookingStatus.WAITING);
    }

    @Test
    void toBookingDto_ShouldMapCorrectly() {
        BookingDto result = BookingMapper.toBookingDto(booking);

        assertThat(result)
                .isNotNull()
                .extracting(
                        BookingDto::getId,
                        BookingDto::getStart,
                        BookingDto::getEnd,
                        BookingDto::getItemId)
                .containsExactly(
                        booking.getId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getItem().getId());
    }

    @Test
    void toBookingDtoResponse_ShouldMapCorrectly() {
        BookingDtoResponse result = BookingMapper.toBookingDtoResponse(booking);

        assertThat(result)
                .isNotNull()
                .extracting(
                        BookingDtoResponse::getId,
                        BookingDtoResponse::getStart,
                        BookingDtoResponse::getEnd,
                        res -> res.getItem().getId(),
                        res -> res.getBooker().getId(),
                        BookingDtoResponse::getStatus)
                .containsExactly(
                        booking.getId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getItem().getId(),
                        booking.getBooker().getId(),
                        booking.getStatus());
    }
}
