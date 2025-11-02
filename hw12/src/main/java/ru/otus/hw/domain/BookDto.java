package ru.otus.hw.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record BookDto(long id, @Length(min = 3, max = 255) String title,
                      @NotNull AuthorDto author, @NotNull @NotEmpty List<GenreDto> genres) {
}
