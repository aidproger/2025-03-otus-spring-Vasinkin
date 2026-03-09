package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

public interface AuthorRepository {

    Flux<Author> findAll();

    Mono<Author> findById(String id);

}
