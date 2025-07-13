package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private BookingDtoResponse bookingDtoResponse;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setStart(bookingDto.getStart());
        bookingDtoResponse.setEnd(bookingDto.getEnd());
    }

    @Test
    void createBooking_ValidBooking_ReturnsBookingDtoResponse() throws Exception {
        when(bookingService.createBooking(anyLong(), any(BookingDto.class))).thenReturn(bookingDtoResponse);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getBooking_ValidBookingId_ReturnsBookingDtoResponse() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingDtoResponse);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getUserBookings_ValidUserId_ReturnsListOfBookingDtoResponse() throws Exception {
        when(bookingService.getUserBookings(anyLong(), any(String.class))).thenReturn(Collections.singletonList(bookingDtoResponse));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
