package ru.otus.hw.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с комментариями в виде dto ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({CommentServiceImpl.class, JpaCommentRepository.class,
        JpaBookRepository.class})
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private List<CommentDto> dtoComments;

    @BeforeEach
    void setUp() {
        dtoComments = TestDataGenerator.generateExpectedDbCommentsByBookId(NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
    }

    @DisplayName("должен загружать список комментариев по id книги в виде dto ")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = commentService.findAllByBookId(FIRST_BOOK_ID);
        var expectedComments = dtoComments;

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий и возвращать dto ")
    @Test
    void shouldSaveNewComment() {
        var returnedComment = commentService.insert("CommentText_10500", FIRST_BOOK_ID);

        assertThat(returnedComment).isNotNull();
        var expectedComment = new CommentDto(returnedComment.id(), "CommentText_10500");

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.id() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        commentService.deleteById(expectedComment.id());
    }

    @DisplayName("должен сохранять измененный комментарий и возвращать dto ")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new CommentDto(SECOND_COMMENT_ID, "CommentText_10500");
        var returnedComment = commentService.update(SECOND_COMMENT_ID, "CommentText_10500", FIRST_BOOK_ID);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.id() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        commentService.update(SECOND_COMMENT_ID, dtoComments.get(1).text(), FIRST_BOOK_ID);
    }

}
