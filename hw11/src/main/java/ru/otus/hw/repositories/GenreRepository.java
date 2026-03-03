package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.Set;

public interface GenreRepository {

    Flux<Genre> findAll();

    Flux<Genre> findAllByIds(Set<String> ids);
}
