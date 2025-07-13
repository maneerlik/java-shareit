package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private AutoCloseable mocks;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        assertEquals(user1, user1);

        assertEquals(user1, user2);
        assertEquals(user2, user1);

        User user4 = new User();
        user4.setId(1L);
        assertEquals(user1, user2);
        assertEquals(user2, user4);
        assertEquals(user1, user4);

        assertEquals(user1, user2);

        assertNotEquals(null, user1);

        assertNotEquals(user1, user3);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void createUser_ValidUser_ReturnsUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void getUser_ValidUserId_ReturnsUserDto() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getAllUsers_ReturnsListOfUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@example.com");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).getName());
    }

    @Test
    void updateUser_ValidUser_ReturnsUserDto() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated@example.com");

        User user = new User();
        user.setId(userId);
        user.setName("User");
        user.setEmail("user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser_ValidUserId_DeletesUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }
}
