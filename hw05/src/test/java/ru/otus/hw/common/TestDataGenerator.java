package ru.otus.hw.common;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

public class TestDataGenerator {

    public static List<Author> generateExpectedDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public static List<Genre> generateExpectedDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    public static List<List<Genre>> generateExpectedDbArraysGenres() {
        return IntStream.range(2, 8).boxed()
                .map(number -> IntStream.range(1, number).boxed()
                        .map(id -> new Genre(id, "Genre_" + id))
                        .toList())
                .toList();
    }

    public static List<Book> generateExpectedDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
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

}
