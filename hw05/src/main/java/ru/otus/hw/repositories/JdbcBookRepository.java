package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        return getBookByIdWithoutGenres(id);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("DELETE FROM books WHERE id=:id", Map.of("id", id));
    }

    private Optional<Book> getBookByIdWithoutGenres(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.query("" +
                            "SELECT bg.book_id, b.title, " +
                            "  b.author_id, a.full_name AS author_full_name, bg.genre_id, g.name " +
                            "FROM books b " +
                            "  LEFT JOIN authors a ON b.author_id=a.id " +
                            "  LEFT JOIN books_genres bg ON b.id=bg.book_id " +
                            "  LEFT JOIN genres g ON bg.genre_id=g.id " +
                            "WHERE b.id=:id",
                    Map.of("id", id),
                    new BookResultSetExtractor()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations.query("" +
                        "SELECT b.id, b.title, b.author_id, a.full_name AS author_full_name " +
                        "FROM books b " +
                        "  LEFT JOIN authors a ON b.author_id=a.id " +
                        "ORDER BY b.id",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations.query("" +
                        "SELECT book_id, genre_id " +
                        "FROM books_genres " +
                        "ORDER BY book_id",
                new DataClassRowMapper<>(BookGenreRelation.class));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        var mapGenres = convertListGenresToMap(genres);
        for (int i = 0, j = 0; i < booksWithoutGenres.size() && j < relations.size(); ) {
            var book = booksWithoutGenres.get(i);
            var bookGenreRelation = relations.get(j);
            if (book.getId() < bookGenreRelation.bookId) {
                i++;
            } else if (book.getId() == bookGenreRelation.bookId) {
                Genre genre = mapGenres.get(bookGenreRelation.genreId());
                book.getGenres().add(genre);
                j++;
            } else {
                j++;
            }
        }
    }

    private Map<Long, Genre> convertListGenresToMap(List<Genre> genres) {
        return genres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        var params = new MapSqlParameterSource();
        params.addValues(Map.of("title", book.getTitle(),
                "author_id", book.getAuthor().getId()));

        namedParameterJdbcOperations.update("" +
                        "INSERT INTO books (title, author_id) " +
                        "VALUES (:title, :author_id)",
                params, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = Map.of("id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId());
        int updateRowCount = namedParameterJdbcOperations.update("" +
                        "UPDATE books SET title=:title, author_id=:author_id " +
                        "WHERE id=:id",
                params);
        if (updateRowCount == 0) {
            throw new EntityNotFoundException("Book id '" + book.getId() + "' not found");
        }

        boolean needUpdateGenres = checkNeedUpdateGenresRelationsFor(book);
        if (needUpdateGenres) {
            removeGenresRelationsFor(book);
            batchInsertGenresRelationsFor(book);
        }

        return book;
    }

    private boolean checkNeedUpdateGenresRelationsFor(Book book) {
        var genres = book.getGenres();
        var ids = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        var params = Map.of("book_id", book.getId(), "ids", ids);

        int countGenresRelationsInDB = namedParameterJdbcOperations.queryForObject("" +
                        "SELECT count(genre_id) " +
                        "FROM books_genres " +
                        "WHERE book_id=:book_id AND genre_id IN (:ids)",
                params, Integer.class);

        return countGenresRelationsInDB != genres.size();
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var batch = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValues(Map.of("book_id", book.getId(), "genre_id", genre.getId())))
                .toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcOperations.batchUpdate(
                "INSERT INTO books_genres (book_id, genre_id) " +
                        "VALUES (:book_id, :genre_id)", batch);
    }

    private void removeGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.update("DELETE FROM books_genres WHERE book_id=:book_id",
                Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("author_full_name");
            return new Book(id, title, new Author(authorId, authorFullName), new ArrayList<Genre>());
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    long bookId = rs.getLong("book_id");
                    String title = rs.getString("title");
                    long authorId = rs.getLong("author_id");
                    String authorFullName = rs.getString("author_full_name");
                    book = new Book(bookId, title, new Author(authorId, authorFullName), new ArrayList<Genre>());
                }
                long genreId = rs.getLong("genre_id");
                String name = rs.getString("name");
                book.getGenres().add(new Genre(genreId, name));
            }
            return book;
        }
    }


    private record BookGenreRelation(long bookId, long genreId) {
    }
}
