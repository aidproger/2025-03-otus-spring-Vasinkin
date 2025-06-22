package ru.otus.hw.services;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.JpaCommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с комментариями для книг ")
@DataJpaTest
@Import({CommentServiceImpl.class, JpaCommentRepository.class,
        BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    private static final long FIRST_BOOK_ID = 1;

    private static final long FIRST_AUTHOR_ID = 1;

    private static final long NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private static final long NUMBER_OF_GENRES_FOR_FIRST_BOOK_ID = 2;

    @DisplayName("не должен выбрасывать LazyInitializationException для комментариев и книги ")
    @Test
    void shouldNotThrowLazyInitializationExceptionForCommentAndBook() {
        var comments = commentService.findAllByBookId(FIRST_BOOK_ID);

        assertThat(comments.size()).isEqualTo(NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
        assertThat(comments.get(0).book().id()).isEqualTo(FIRST_BOOK_ID);
        assertThat(comments.get(0).book().author().id()).isEqualTo(FIRST_AUTHOR_ID);
        assertThat(comments.get(0).book().genres().size()).isEqualTo(NUMBER_OF_GENRES_FOR_FIRST_BOOK_ID);
    }
}
