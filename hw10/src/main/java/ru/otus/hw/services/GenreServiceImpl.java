package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.exceptions.GenreNotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        var genres = genreRepository.findAll().stream()
                .map(g -> new GenreDto(g.getId(), g.getName()))
                .toList();
        if (genres.isEmpty()) {
            throw new GenreNotFoundException();
        }
        return genres;
    }
}
