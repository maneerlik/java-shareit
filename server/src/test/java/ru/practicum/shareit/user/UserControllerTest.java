package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser_ShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void getUser_ShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        when(userService.getUser(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("User");
        userDto.setEmail("user@example.com");

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User"));
    }

    @Test
    void updateUser_ShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated@example.com");

        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }
}
