package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findByBookerId_ReturnsListOfBookings() {
        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        entityManager.persist(booker);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(booker);
        entityManager.persist(item);

        Booking booking = new Booking();
        booking.setStartDate(LocalDateTime.now().plusDays(1));
        booking.setEndDate(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        entityManager.persist(booking);

        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId(), Sort.unsorted()).stream().toList();

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }
}
