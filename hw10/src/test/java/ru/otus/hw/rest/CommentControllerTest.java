package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.rest.exceptions.CommentNotFoundException;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с комментариями в формате json ")
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private CommentServiceImpl commentService;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private List<CommentDto> dtoComments;

    @BeforeEach
    void setUp() {
        dtoComments = TestDataGenerator.generateExpectedDbCommentsByBookId(NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
    }

    @DisplayName("должен возвращать корректный список комментариев к заданной книге в формате json ")
    @Test
    void shouldReturnCorrectCommentsListByBookId() throws Exception {
        given(commentService.findAllByBookId(FIRST_BOOK_ID)).willReturn(dtoComments);
        mvc.perform(get("/api/v1/books/{bookId}/comments", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoComments)));
    }

    @DisplayName("должен возвращать заданную ошибку, если комментарии не найдены ")
    @Test
    void shouldReturnExpectedErrorWhenCommentsNotFound() throws Exception {
        String expectedErrorText = "Comment not found";

        given(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).willReturn(expectedErrorText);
        given(commentService.findAllByBookId(anyLong())).willThrow(new CommentNotFoundException("Error"));
        mvc.perform(get("/api/v1/books/{bookId}/comments", FIRST_BOOK_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedErrorText));
    }

    @DisplayName("должен удалять комментарий по идентификатору ")
    @Test
    void shouldCorrectlyDeleteCommentById() throws Exception {
        mvc.perform(delete("/api/v1/books/{bookId}/comments/{id}", FIRST_BOOK_ID, SECOND_COMMENT_ID))
                .andExpect(status().isNoContent());
        verify(commentService, times(1)).deleteById(SECOND_COMMENT_ID);
    }

    @DisplayName("должен добавлять комментарий по идентификатору книги ")
    @Test
    void shouldCorrectlyAddCommentByBookId() throws Exception {
        var actualComments = dtoComments.get((int) SECOND_COMMENT_ID - 1);
        String expectedResult = mapper.writeValueAsString(actualComments);

        given(commentService.insert(anyString(), anyLong())).willReturn(actualComments);
        mvc.perform(post("/api/v1/books/{bookId}/comments", FIRST_BOOK_ID)
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        verify(commentService, times(1)).insert(actualComments.text(), FIRST_BOOK_ID);
    }

}
