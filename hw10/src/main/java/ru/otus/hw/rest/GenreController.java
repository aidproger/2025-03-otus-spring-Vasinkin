package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.rest.exceptions.GenreNotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getAllGenres() {
        List<GenreDto> genres = genreService.findAll();
        if (genres.isEmpty()) {
            throw new GenreNotFoundException();
        }
        return genres;
    }

}
