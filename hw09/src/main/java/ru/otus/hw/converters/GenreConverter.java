package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.id(), genre.name());
    }
}
