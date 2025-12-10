package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final Logger log = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getAllGenres() {
        log.info("Request all data");
        return genreService.findAll();
    }

}
