package ru.otus.hw.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

@Component
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

}
