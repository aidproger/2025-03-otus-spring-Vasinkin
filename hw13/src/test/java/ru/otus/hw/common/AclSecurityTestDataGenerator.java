package ru.otus.hw.common;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Set;
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
                Arguments.of("login_1", false, "ru.otus.hw.services.BookService", "update", new Class<?>[]{long.class, String.class, long.class, Set.class}, new Object[]{1L, "", 1L, Set.of(1L)}),
                Arguments.of("login_2", true, "ru.otus.hw.services.BookService", "update", new Class<?>[]{long.class, String.class, long.class, Set.class}, new Object[]{1L, "", 1L, Set.of(1L)}),
                Arguments.of("login_1", false, "ru.otus.hw.services.BookService", "deleteById", new Class<?>[]{long.class}, new Object[]{1L}),
                Arguments.of("login_4", true, "ru.otus.hw.services.BookService", "deleteById", new Class<?>[]{long.class}, new Object[]{1L})
        );
    }

    private static Stream<Arguments> getCommentAclSecurityTestData() {
        return Stream.of(
                Arguments.of("login_2", false, "ru.otus.hw.services.CommentService", "update", new Class<?>[]{long.class, String.class, long.class}, new Object[]{8L, "", 3L}),
                Arguments.of("login_3", true, "ru.otus.hw.services.CommentService", "update", new Class<?>[]{long.class, String.class, long.class}, new Object[]{8L, "", 3L}),
                Arguments.of("login_1", false, "ru.otus.hw.services.CommentService", "deleteById", new Class<?>[]{long.class}, new Object[]{1L}),
                Arguments.of("login_3", true, "ru.otus.hw.services.CommentService", "deleteById", new Class<?>[]{long.class}, new Object[]{1L})
        );
    }

}
