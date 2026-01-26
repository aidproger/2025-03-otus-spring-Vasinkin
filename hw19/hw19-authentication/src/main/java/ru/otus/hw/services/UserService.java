package ru.otus.hw.services;

import ru.otus.hw.domain.UserDto;
import ru.otus.hw.domain.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto insert(UserResponseDto userResponseDto);

    UserDto update(UserResponseDto userResponseDto);

    void deleteById(long id);
}
