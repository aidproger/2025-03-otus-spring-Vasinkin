package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с комментариями в виде dto ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(CommentServiceImpl.class)
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private MongoOperations mongoOperations;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 3;

    private List<CommentDto> dtoComments;

    private String insertCommentId;

    @BeforeEach
    void setUp() {
        dtoComments = TestDataGenerator.generateExpectedDbCommentsByBookId(NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
    }

    @AfterEach
    void tearDown() {
        if (insertCommentId != null) {
            mongoOperations.remove(
                    new Query(Criteria.where("_id").is(insertCommentId)), Comment.class);
            insertCommentId = null;
        }

        var book = mongoOperations.findById(String.valueOf(FIRST_BOOK_ID), Book.class);

        var query = new Query(Criteria.where("_id").is(String.valueOf(SECOND_COMMENT_ID)));
        var update = new Update();
        update.set("text", dtoComments.get(1).text());
        update.set("book", book);
        mongoOperations.updateFirst(query, update, Comment.class);
    }

    @DisplayName("должен загружать список комментариев по id книги в виде dto ")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = commentService.findAllByBookId(String.valueOf(FIRST_BOOK_ID));
        var expectedComments = dtoComments;

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен загружать комментарий по id в виде dto ")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComments = commentService.findById(String.valueOf(SECOND_COMMENT_ID));
        var expectedComments = dtoComments.get((int) SECOND_COMMENT_ID - 1);

        assertThat(actualComments).isPresent().get().isEqualTo(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий и возвращать dto ")
    @Test
    void shouldSaveNewComment() {
        var returnedComment = commentService.insert("CommentText_10500", String.valueOf(FIRST_BOOK_ID));

        assertThat(returnedComment).isNotNull();
        insertCommentId = returnedComment.id();
        var expectedComment = new CommentDto(returnedComment.id(), "CommentText_10500");

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.id() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

    }

    @DisplayName("должен сохранять измененный комментарий и возвращать dto ")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new CommentDto(String.valueOf(SECOND_COMMENT_ID), "CommentText_10500");
        var returnedComment = commentService.update(
                String.valueOf(SECOND_COMMENT_ID), "CommentText_10500", String.valueOf(FIRST_BOOK_ID));

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.id() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        var expectedBook = mongoOperations.findById(FIRST_BOOK_ID, Book.class);
        var expectedComment = mongoOperations.insert(
                new Comment(null, "CommentForDelete", expectedBook));

        assertThat(mongoOperations.findById(expectedComment.getId(), Comment.class)).isNotNull();
        commentService.deleteById(expectedComment.getId());
        assertThat(mongoOperations.findById(expectedComment.getId(), Comment.class)).isNull();
    }

}
