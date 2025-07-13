package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }


    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get(String.format("/%s", bookingId), userId);
    }

    public ResponseEntity<Object> getUserBookings(Long userId, BookingState state) {
        return get(String.format("?state=%s", state), userId);
    }

    public ResponseEntity<Object> getOwnerBookings(Long ownerId, BookingState state) {
        return get(String.format("/owner?state=%s)", state), ownerId);
    }

    public ResponseEntity<Object> updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        return patch(String.format("/%s?approved=%s", bookingId, approved), userId, null);
    }
}
