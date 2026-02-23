package ru.otus.hw.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.handlers.CommentRepositoryEventHandler;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с комментариями в формате json ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private CommentRepository commentRepository;

    @SpyBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepositoryEventHandler commentRepositoryEventHandler;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @BeforeEach
    void setUp() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/$", "");
    }

    @DisplayName("должен возвращать корректный список комментариев к заданной книге в формате json ")
    @Test
    void shouldReturnCorrectCommentsListByBookId() throws Exception {

        var returnBook = new Book(FIRST_BOOK_ID, "Book_1", new Author(), List.of(new Genre()),
                List.of(new Comment(1L, "Comment_1", null, new Book())));

        doReturn(Optional.of(returnBook)).when(bookRepository).findById(FIRST_BOOK_ID);

        mvc.perform(get("%s/books/{bookId}/comments".formatted(dataRestBasePath), FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.comments", hasSize(returnBook.getComments().size())));

        verify(bookRepository, times(1)).findById(FIRST_BOOK_ID);
    }

    @DisplayName("должен удалять комментарий по идентификатору ")
    @Test
    void shouldCorrectlyDeleteCommentById() throws Exception {
        doNothing().when(commentRepository).deleteById(any(Long.class));

        mvc.perform(delete("%s/comments/{id}".formatted(dataRestBasePath), SECOND_COMMENT_ID))
                .andExpect(status().isNoContent());

        verify(commentRepository, times(1)).deleteById(SECOND_COMMENT_ID);
    }

    @DisplayName("должен добавлять комментарий по идентификатору книги ")
    @Test
    void shouldCorrectlyAddCommentByBookId() throws Exception {
        var expectedComment = new Comment(0L, "Comment_test", null, new Book());
        var savedComment = new Comment(10L, "Comment_test", null, new Book());
        var jsonComment = "{\"text\":\"%s\"}".formatted(expectedComment.getText());

        doReturn(savedComment).when(commentRepository).save(any(Comment.class));
        mvc.perform(post("%s/comments".formatted(dataRestBasePath))
                        .contentType(APPLICATION_JSON)
                        .content(jsonComment))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("comments/%d".formatted(savedComment.getId()))));
        verify(commentRepository, times(1)).save(expectedComment);
    }

}
