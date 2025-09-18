package ru.otus.hw.domain;

import java.util.Objects;

public record GenreDto(long id, String name) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenreDto genreDto)) {
            return false;
        }
        return id == genreDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
