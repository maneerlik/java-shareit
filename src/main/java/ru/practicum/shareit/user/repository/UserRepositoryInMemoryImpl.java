package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;


    @Override
    public User save(User user) {
        if (isExistsEmail(user))
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already exists");
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (isExistsEmail(user))
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already exists");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    public boolean isExistsEmail(User user) {
        String email = user.getEmail();
        if (email == null) return false;
        if (users.values().isEmpty()) return false;
        return users.values().stream()
                .anyMatch(u -> email.equals(u.getEmail()));
    }
}
