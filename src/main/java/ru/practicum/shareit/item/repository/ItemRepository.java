package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long id);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', :text, '%')) " +
            "   or upper(i.description) like upper(concat('%', :text, '%'))")
    List<Item> search(@Param("text") String text);

}
