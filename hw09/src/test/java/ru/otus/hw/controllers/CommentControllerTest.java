package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер взаимодействия с комментариями через html страницы ")
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private BookServiceImpl bookService;

    private static final long SECOND_COMMENT_ID = 2L;

    private static final long FIRST_BOOK_ID = 1;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
        dtoBooks = TestDataGenerator.generateExpectedDtoBooks(dtoAuthors, dtoGenres);
    }

    @DisplayName("должен отдавать страницу создания комментария с правильными представлением и моделью данных ")
    @Test
    void shouldRenderCreatePageWithCorrectViewAndModelAttributes() throws Exception {
        var expectedBook = dtoBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedCommentEmpty = new CommentDto(0, "");
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(expectedBook));
        mvc.perform(get("/createcomment").param("bookid", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(view().name("createComment"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comment", expectedCommentEmpty));
    }

    @DisplayName("должен сохранять новый комментарий и перенаправлять на верный url ")
    @Test
    void shouldSaveCommentAndRedirectToContextPath() throws Exception {
        var newComment = new CommentDto(0, "comment_10");
        mvc.perform(post("/editcomment")
                        .param("id", String.valueOf(newComment.id()))
                        .param("text", newComment.text())
                        .param("bookid", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(view().name("redirect:/book/" + FIRST_BOOK_ID));
        verify(commentService, times(1)).insert(newComment.text(), FIRST_BOOK_ID);
    }

    @DisplayName("должен удалять комментарий и перенаправлять на верный url ")
    @Test
    void shouldDeleteCommentAndRedirectToContextPath() throws Exception {
        mvc.perform(post("/deletecomment/{id}", SECOND_COMMENT_ID)
                        .param("bookid", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(view().name("redirect:/book/" + FIRST_BOOK_ID));
        verify(commentService, times(1)).deleteById(SECOND_COMMENT_ID);
    }

    @DisplayName("должен отдавать страницу ошибки ")
    @Test
    void shouldRenderErrorPageWhenCommentNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Comment with id %d not found".formatted(SECOND_COMMENT_ID)))
                .when(commentService).deleteById(SECOND_COMMENT_ID);
        mvc.perform(post("/deletecomment/{id}", SECOND_COMMENT_ID)
                        .param("bookid", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(view().name("customError"));
    }
}
