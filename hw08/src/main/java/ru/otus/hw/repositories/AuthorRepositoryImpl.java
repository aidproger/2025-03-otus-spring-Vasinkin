package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final MongoOperations mongoOperations;

    @Override
    public List<Author> findAll() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("author"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.full_name").as("full_name"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "full_name"))
        );

        AggregationResults<Author> results =
                mongoOperations.aggregate(aggregation, "books", Author.class);

        return results.getMappedResults();
    }

    @Override
    public Optional<Author> findById(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("author._id").is(id)),
                Aggregation.group("author"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.full_name").as("full_name")
        );

        AggregationResults<Author> results =
                mongoOperations.aggregate(aggregation, "books", Author.class);

        return results.getMappedResults().stream().findFirst();
    }

}
