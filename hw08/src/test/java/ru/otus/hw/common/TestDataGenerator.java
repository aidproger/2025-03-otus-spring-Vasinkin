package ru.otus.hw.common;

import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.models.Author;
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

    public static List<AuthorDto> generateExpectedDtoAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(String.valueOf(id), "Author_" + id))
                .toList();
    }

    public static List<GenreDto> generateExpectedDtoGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    public static List<BookDto> generateExpectedDtoBooks(List<AuthorDto> dtoAuthors, List<GenreDto> dtoGenres) {
        return IntStream.range(1, 5).boxed()
                .map(id -> new BookDto(String.valueOf(id),
                        "BookTitle_" + id,
                        id == 4 ? dtoAuthors.get(1) : dtoAuthors.get(id - 1),
                        dtoGenres.subList(id == 4 ? (2 - 1) * 2 : (id - 1) * 2, id == 4 ? (2 - 1) * 2 + 2 : (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDto> generateExpectedDbCommentsByBookId(int numberOfComments) {
        return IntStream.range(1, numberOfComments + 1).boxed()
                .map(id -> new CommentDto(String.valueOf(id), "comment_" + id))
                .toList();
    }

}
