package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository {

    Flux<Genre> findAll();

    Mono<List<Genre>> findAllByIds(Set<String> ids);
}
