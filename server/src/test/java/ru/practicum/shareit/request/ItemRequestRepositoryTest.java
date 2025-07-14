package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void testFindByRequestorId() {
        User requestor = new User();
        requestor.setName("Requestor");
        requestor.setEmail("requestor@example.com");
        entityManager.persist(requestor);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Request Description");
        itemRequest.setRequestor(requestor);
        entityManager.persist(itemRequest);

        List<ItemRequest> requests = itemRequestRepository.findByRequestorId(requestor.getId(), Sort.unsorted());

        assertEquals(1, requests.size());
        assertEquals("Request Description", requests.get(0).getDescription());
    }
}
