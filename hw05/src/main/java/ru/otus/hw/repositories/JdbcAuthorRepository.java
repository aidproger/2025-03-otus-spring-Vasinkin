package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        try {
            return namedParameterJdbcOperations.query("SELECT id, full_name FROM authors",
                    new AuthorRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.
                    queryForObject("SELECT id, full_name FROM authors WHERE id=:id",
                            Map.of("id", id),
                            new AuthorRowMapper()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
