package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final MongoOperations mongoOperations;

    @Override
    public List<Genre> findAll() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("genres"),
                Aggregation.group("genres"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.name").as("name"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "name")));

        AggregationResults<Genre> results =
                mongoOperations.aggregate(aggregation, "books", Genre.class);

        return results.getMappedResults();
    }

    @Override
    public List<Genre> findAllByIds(Set<String> ids) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("genres"),
                Aggregation.match(Criteria.where("genres._id").in(ids)),
                Aggregation.group("genres"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("_id._id").as("_id")
                        .and("_id.name").as("name"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "name")));

        AggregationResults<Genre> results =
                mongoOperations.aggregate(aggregation, "books", Genre.class);

        return results.getMappedResults();
    }
}
