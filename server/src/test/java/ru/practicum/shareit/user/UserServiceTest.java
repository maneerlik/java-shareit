package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
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

    @Mock private UserRepository userRepository;

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
        User userOne = new User();
        userOne.setId(1L);

        User userTwo = new User();
        userTwo.setId(1L);

        User userThree = new User();
        userThree.setId(2L);

        assertEquals(userOne, userOne);

        assertEquals(userOne, userTwo);
        assertEquals(userTwo, userOne);

        User userFour = new User();
        userFour.setId(1L);
        assertEquals(userOne, userTwo);
        assertEquals(userTwo, userFour);
        assertEquals(userOne, userFour);

        assertEquals(userOne, userTwo);

        assertNotEquals(null, userOne);

        assertNotEquals(userOne, userThree);

        assertEquals(userOne.hashCode(), userTwo.hashCode());
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

    @Test
    void testToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        User user = UserMapper.toUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void testToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@example.com");

        UserDto userDto = UserMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }
}
