package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = TestDataGenerator.generateExpectedDbCommentsByBookId(em,
                (int) FIRST_BOOK_ID, NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
    }

    @DisplayName("должен загружать комментарий по id ")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = em.find(Comment.class, SECOND_COMMENT_ID);
        var actualComment = repositoryJpa.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get().isEqualTo(expectedComment);
        System.out.println(actualComment);
    }

    @DisplayName("должен загружать список комментариев по id книги ")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = repositoryJpa.findAllByBookId(FIRST_BOOK_ID);
        var expectedComments = dbComments;

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

}
