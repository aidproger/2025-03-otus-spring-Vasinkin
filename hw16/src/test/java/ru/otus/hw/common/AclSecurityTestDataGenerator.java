package ru.otus.hw.common;

import org.junit.jupiter.params.provider.Arguments;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Stream;

public class AclSecurityTestDataGenerator {

    public static Stream<Arguments> getAclSecurityTestData() {
        return Stream.of(
                        getBookAclSecurityTestData(),
                        getCommentAclSecurityTestData()
                )
                .flatMap(s -> s);
    }

    //username, accessDenied, className, methodName, argsClazz, args
    private static Stream<Arguments> getBookAclSecurityTestData() {
        return Stream.of(
                Arguments.of("login_1", false, "ru.otus.hw.handlers.BookRepositoryEventHandler", "validateBookCreate",
                        new Class<?>[]{Book.class}, new Object[]{new Book(1L, "", new Author(), List.of(new Genre()), List.of(new Comment()))}),
                Arguments.of("login_2", true, "ru.otus.hw.handlers.BookRepositoryEventHandler", "validateBookCreate",
                        new Class<?>[]{Book.class}, new Object[]{new Book(1L, "", new Author(), List.of(new Genre()), List.of(new Comment()))}),
                Arguments.of("login_1", false, "ru.otus.hw.handlers.BookRepositoryEventHandler", "validateDeleteBookDelete",
                        new Class<?>[]{Book.class}, new Object[]{new Book(1L, "", new Author(), List.of(new Genre()), List.of(new Comment()))}),
                Arguments.of("login_4", true, "ru.otus.hw.handlers.BookRepositoryEventHandler", "validateDeleteBookDelete",
                        new Class<?>[]{Book.class}, new Object[]{new Book(1L, "", new Author(), List.of(new Genre()), List.of(new Comment()))})
        );
    }

    private static Stream<Arguments> getCommentAclSecurityTestData() {
        return Stream.of(
                Arguments.of("login_2", false, "ru.otus.hw.handlers.CommentRepositoryEventHandler", "validateCommentCreate",
                        new Class<?>[]{Comment.class}, new Object[]{new Comment(8L, "", null, new Book())}),
                Arguments.of("login_3", true, "ru.otus.hw.handlers.CommentRepositoryEventHandler", "validateCommentCreate",
                        new Class<?>[]{Comment.class}, new Object[]{new Comment(8L, "", null, new Book())}),
                Arguments.of("login_1", false, "ru.otus.hw.handlers.CommentRepositoryEventHandler", "validateDeleteCommentDelete",
                        new Class<?>[]{Comment.class}, new Object[]{new Comment(1L, "", null, new Book())}),
                Arguments.of("login_3", true, "ru.otus.hw.handlers.CommentRepositoryEventHandler", "validateDeleteCommentDelete",
                        new Class<?>[]{Comment.class}, new Object[]{new Comment(1L, "", null, new Book())})
        );
    }

}
