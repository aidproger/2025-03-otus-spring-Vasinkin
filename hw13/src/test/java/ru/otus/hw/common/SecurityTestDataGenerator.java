package ru.otus.hw.common;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class SecurityTestDataGenerator {

    public static Stream<Arguments> getSecurityTestData() {
        return Stream.of(
                        getBookPageSecurityTestData(),
                        getCommentPageSecurityTestData(),
                        getLoginPageSecurityTestData(),
                        getAuthorRestSecurityTestData(),
                        getGenreRestSecurityTestData(),
                        getCommentRestSecurityTestData(),
                        getBookRestSecurityTestData())
                .flatMap(s -> s);
    }

    private static Stream<Arguments> getBookPageSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/", null, 302, true),
                Arguments.of("get", "/", "login_1", 200, false),
                Arguments.of("get", "/book/1", null, 302, true),
                Arguments.of("get", "/book/1", "login_1", 200, false),
                Arguments.of("get", "/createbook", null, 302, true),
                Arguments.of("get", "/createbook", "login_1", 200, false),
                Arguments.of("get", "/editbook/1", null, 302, true),
                Arguments.of("get", "/editbook/1", "login_1", 200, false)
        );
    }

    private static Stream<Arguments> getCommentPageSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/createcomment", null, 302, true),
                Arguments.of("get", "/createcomment", "login_1", 400, false)
        );
    }

    private static Stream<Arguments> getLoginPageSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/login", null, 200, false),
                Arguments.of("get", "/login", "login_1", 200, false)
        );
    }

    private static Stream<Arguments> getAuthorRestSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/api/v1/authors", null, 302, true),
                Arguments.of("get", "/api/v1/authors", "login_1", 200, false)
        );
    }

    private static Stream<Arguments> getGenreRestSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/api/v1/genres", null, 302, true),
                Arguments.of("get", "/api/v1/genres", "login_1", 200, false)
        );
    }

    private static Stream<Arguments> getCommentRestSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/api/v1/books/1/comments", null, 302, true),
                Arguments.of("get", "/api/v1/books/1/comments", "login_1", 200, false),
                Arguments.of("delete", "/api/v1/books/1/comments/2", null, 302, true),
                Arguments.of("delete", "/api/v1/books/1/comments/2", "login_1", 204, false),
                Arguments.of("post", "/api/v1/books/1/comments", null, 302, true),
                Arguments.of("post", "/api/v1/books/1/comments", "login_1", 400, false)
        );
    }

    private static Stream<Arguments> getBookRestSecurityTestData() {
        return Stream.of(
                Arguments.of("get", "/api/v1/books", null, 302, true),
                Arguments.of("get", "/api/v1/books", "login_1", 200, false),
                Arguments.of("get", "/api/v1/books/2", null, 302, true),
                Arguments.of("get", "/api/v1/books/2", "login_1", 404, false),
                Arguments.of("delete", "/api/v1/books/2", null, 302, true),
                Arguments.of("delete", "/api/v1/books/2", "login_1", 204, false),
                Arguments.of("post", "/api/v1/books", null, 302, true),
                Arguments.of("post", "/api/v1/books", "login_1", 400, false),
                Arguments.of("put", "/api/v1/books", null, 302, true),
                Arguments.of("put", "/api/v1/books", "login_1", 400, false)
        );
    }

}
