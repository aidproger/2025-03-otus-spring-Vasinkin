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
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookServiceImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с книгами в формате json ")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private BookServiceImpl bookService;

    private List<BookDto> dtoBooks;

    private static final long SECOND_BOOK_ID = 2L;

    @BeforeEach
    void setUp() {
        var dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
        var dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
        dtoBooks = TestDataGenerator.generateExpectedDtoBooks(dtoAuthors, dtoGenres);
    }

    @DisplayName("должен возвращать корректный список книг в формате json ")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        given(bookService.findAll()).willReturn(dtoBooks);
        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoBooks)));
    }

    @DisplayName("должен возвращать заданную ошибку, если книги не найдены ")
    @Test
    void shouldReturnExpectedErrorWhenBooksNotFound() throws Exception {
        String expectedErrorText = "Comment not found";

        given(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).willReturn(expectedErrorText);
        given(bookService.findAll()).willThrow(new BookNotFoundException("Error"));
        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedErrorText));
    }

    @DisplayName("должен возвращать книгу по заданному идентификатору в формате json ")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        var expectedBook = dtoBooks.get((int) SECOND_BOOK_ID - 1);
        var actualBook = Optional.of(expectedBook);

        given(bookService.findById(SECOND_BOOK_ID)).willReturn(actualBook);
        mvc.perform(get("/api/v1/books/{id}", SECOND_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @DisplayName("должен удалять книгу по идентификатору ")
    @Test
    void shouldCorrectlyDeleteBookById() throws Exception {
        mvc.perform(delete("/api/v1/books/{id}", SECOND_BOOK_ID))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(SECOND_BOOK_ID);
    }

    @DisplayName("должен добавлять книгу ")
    @Test
    void shouldCorrectlyAddBook() throws Exception {
        var actualBook = dtoBooks.get((int) SECOND_BOOK_ID - 1);
        String expectedBook = mapper.writeValueAsString(actualBook);
        var expectedGenresIds = actualBook.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());

        given(bookService.insert(anyString(), anyLong(), any())).willReturn(actualBook);
        mvc.perform(post("/api/v1/books")
                        .contentType(APPLICATION_JSON)
                        .content(expectedBook))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBook));
        verify(bookService, times(1))
                .insert(actualBook.title(), actualBook.author().id(), expectedGenresIds);
    }

    @DisplayName("должен обновлять книгу ")
    @Test
    void shouldCorrectlyUpdateBook() throws Exception {
        var actualBook = dtoBooks.get((int) SECOND_BOOK_ID - 1);
        String expectedBook = mapper.writeValueAsString(actualBook);
        var expectedGenresIds = actualBook.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());

        given(bookService.update(anyLong(), anyString(), anyLong(), any())).willReturn(actualBook);
        mvc.perform(put("/api/v1/books")
                        .contentType(APPLICATION_JSON)
                        .content(expectedBook))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBook));
        verify(bookService, times(1))
                .update(actualBook.id(), actualBook.title(), actualBook.author().id(), expectedGenresIds);
    }

}
