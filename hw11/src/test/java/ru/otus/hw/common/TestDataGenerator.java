package ru.otus.hw.common;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

public class TestDataGenerator {

    public static List<Author> generateExpectedDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.valueOf(id), "Author_" + id))
                .toList();
    }

    public static List<Genre> generateExpectedDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    public static List<Book> generateExpectedDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(String.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<Book> generateExpectedDbBooks() {
        var dbAuthors = generateExpectedDbAuthors();
        var dbGenres = generateExpectedDbGenres();
        return generateExpectedDbBooks(dbAuthors, dbGenres);
    }

    public static List<Comment> generateExpectedDbCommentsByBookId(String bookId) {
        Book book = new Book(bookId, "BookTitle_1", new Author("1", "Author_1"),
                List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2")));
        return IntStream.range(1, 5).boxed()
                .map(id -> new Comment(String.valueOf(id), "comment_" + id, book))
                .toList();
    }

}
