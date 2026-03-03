package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final ReactiveMongoOperations reactiveMongoOperations;

    @Override
    public Flux<Author> findAll() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("author"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.full_name").as("full_name"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "full_name"))
        );

        return reactiveMongoOperations.aggregate(aggregation, "books", Author.class);
    }

    @Override
    public Mono<Author> findById(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("author._id").is(id)),
                Aggregation.group("author"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.full_name").as("full_name")
        );

        return reactiveMongoOperations.aggregate(aggregation, "books", Author.class)
                .next()
                .switchIfEmpty(Mono.error(new AuthorNotFoundException("Author with id %s not found".formatted(id))));
    }

}
