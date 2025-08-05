package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе DATA JPA для работы с комментариями ")
@DataJpaTest
public class DataJpaCommentRepositoryTest {

    @Autowired
    private DataJpaCommentRepository repositoryDataJpa;

    @Autowired
    private TestEntityManager em;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    private List<Comment> dbComments;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbComments = TestDataGenerator.generateExpectedDbCommentsByBookId(em,
                (int) FIRST_BOOK_ID, NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
        dbBooks = TestDataGenerator.generateExpectedDbBooks(em, EXPECTED_NUMBER_OF_BOOKS);
    }

    @DisplayName("должен загружать комментарий по id ")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = em.find(Comment.class, SECOND_COMMENT_ID);
        var actualComment = repositoryDataJpa.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get().isEqualTo(expectedComment);
        System.out.println(actualComment);
    }

    @DisplayName("должен загружать список комментариев по id книги ")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = repositoryDataJpa.findAllByBookId(FIRST_BOOK_ID);
        var expectedComments = dbComments;

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(0, "CommentText_10500", dbBooks.get(0));
        var returnedComment = repositoryDataJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(SECOND_COMMENT_ID, "CommentText_10500", dbBooks.get(0));

        assertThat(em.find(Comment.class, SECOND_COMMENT_ID))
                .isNotEqualTo(expectedComment);

        var returnedComment = repositoryDataJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        var comment = Optional.ofNullable(em.find(Comment.class, SECOND_COMMENT_ID));
        assertThat(comment).isPresent();
        repositoryDataJpa.delete(comment.get());

        comment = Optional.ofNullable(em.find(Comment.class, SECOND_COMMENT_ID));
        assertThat(comment).isEmpty();
    }

}
