package ru.otus.hw.services;

import ru.otus.hw.domain.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
