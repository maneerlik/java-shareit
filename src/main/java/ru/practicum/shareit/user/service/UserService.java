package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long id);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
