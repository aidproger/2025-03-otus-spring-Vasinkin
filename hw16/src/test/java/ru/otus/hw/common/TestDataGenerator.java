package ru.otus.hw.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        return book.getComments();
    }


    public static List<User> generateExpectedDbUsers(TestEntityManager em, int numberOfUsers) {
        return IntStream.range(1, numberOfUsers + 1).boxed()
                .map(id -> em.find(User.class, id))
                .toList();
    }

    public static List<Comment> generateListEmptyComments() {
        return IntStream.range(1, 2).boxed()
                .map(id -> new Comment())
                .toList();
    }

    public static Comment generateEmptyComment() {
        return new Comment();
    }

    public static Optional<Comment> generateOptionalEmptyComment() {
        return Optional.of(new Comment());
    }

    public static Optional<Book> generateOptionalEmptyBook() {
        return Optional.of(new Book());
    }

    public static List<Book> generateListEmptyBooks() {
        return IntStream.range(1, 2).boxed()
                .map(id -> new Book())
                .toList();
    }

    public static Optional<Author> generateOptionalEmptyAuthor() {
        return Optional.of(new Author());
    }

    public static List<Genre> generateListEmptyGenres() {
        return IntStream.range(1, 2).boxed()
                .map(id -> new Genre())
                .toList();
    }

    public static List<Author> generateListEmptyAuthors() {
        return IntStream.range(1, 2).boxed()
                .map(id -> new Author())
                .toList();
    }

    public static Comment generateComment() {
        return new Comment(10L, "",
                LocalDateTime.of(2026, 1, 1, 0, 0, 0), new Book());
    }

}
