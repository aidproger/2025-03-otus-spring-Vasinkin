package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Genre> findAll() {
        try {
            return namedParameterJdbcOperations.query("SELECT id, name FROM genres ORDER BY id",
                    new GenreRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        try {
            return namedParameterJdbcOperations.
                    query("SELECT id, name FROM genres WHERE id IN (:ids)",
                            Map.of("ids", ids),
                            new GenreRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
