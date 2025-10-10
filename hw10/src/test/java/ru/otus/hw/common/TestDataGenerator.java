package ru.otus.hw.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

public class TestDataGenerator {

    public static List<Author> generateExpectedDbAuthors(TestEntityManager em, int numberOfAuthors) {
        return IntStream.range(1, numberOfAuthors + 1).boxed()
                .map(id -> em.find(Author.class, id))
                .toList();
    }

    public static List<Genre> generateExpectedDbGenres(TestEntityManager em, int numberOfGenre) {
        return IntStream.range(1, numberOfGenre + 1).boxed()
                .map(id -> em.find(Genre.class, id))
                .toList();
    }

    public static List<Book> generateExpectedDbBooks(TestEntityManager em, int numberOfBook) {
        return IntStream.range(1, numberOfBook + 1).boxed()
                .map(id -> em.find(Book.class, id))
                .toList();
    }

    public static List<Comment> generateExpectedDbCommentsByBookId(TestEntityManager em,
                                                                   int bookId, int numberOfComments) {
        var book = em.find(Book.class, bookId);
        return IntStream.range(1, numberOfComments + 1).boxed()
                .map(id -> new Comment(id, "comment_" + id, book))
                .toList();
    }

    public static List<AuthorDto> generateExpectedDtoAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    public static List<GenreDto> generateExpectedDtoGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    public static List<BookDto> generateExpectedDtoBooks(List<AuthorDto> dtoAuthors, List<GenreDto> dtoGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dtoAuthors.get(id - 1),
                        dtoGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDto> generateExpectedDbCommentsByBookId(int numberOfComments) {
        return IntStream.range(1, numberOfComments + 1).boxed()
                .map(id -> new CommentDto(id, "comment_" + id))
                .toList();
    }

}
