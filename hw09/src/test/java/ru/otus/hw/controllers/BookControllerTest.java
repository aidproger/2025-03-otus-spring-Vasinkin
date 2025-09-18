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
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер взаимодействия с книгами через html страницы ")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    private static final long FIRST_BOOK_ID = 1;

    private static final int NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID = 4;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    private List<CommentDto> dtoComments;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
        dtoBooks = TestDataGenerator.generateExpectedDtoBooks(dtoAuthors, dtoGenres);
        dtoComments = TestDataGenerator.generateExpectedDbCommentsByBookId(NUMBER_OF_COMMENTS_FOR_FIRST_BOOK_ID);
    }


    @DisplayName("должен отдавать страницу отображения книг, авторов и жанров с правильными представлением и моделью данных ")
    @Test
    void shouldRenderBooksListPageWithCorrectViewAndModelAttributes() throws Exception {
        var expectedBooks = dtoBooks;
        var expectedAuthors = dtoAuthors;
        var expectedGenres = dtoGenres;
        when(bookService.findAll()).thenReturn(expectedBooks);
        when(authorService.findAll()).thenReturn(expectedAuthors);
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/"))
                .andExpect(view().name("listBook"))
                .andExpect(model().attribute("books", expectedBooks))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @DisplayName("должен отдавать страницу отображения книги с комментариями с правильными представлением и моделью данных ")
    @Test
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        var expectedBook = dtoBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedComments = dtoComments;
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(expectedBook));
        when(commentService.findAllByBookId(FIRST_BOOK_ID)).thenReturn(expectedComments);
        mvc.perform(get("/book/" + FIRST_BOOK_ID))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments));
    }

    @DisplayName("должен отдавать страницу создания книги с правильными представлением и моделью данных ")
    @Test
    void shouldRenderCreateBookPageWithCorrectViewAndModelAttributes() throws Exception {
        var expectedBook = new BookDto(0, "", new AuthorDto(0, ""), List.of());
        var expectedAuthors = dtoAuthors;
        var expectedGenres = dtoGenres;
        when(authorService.findAll()).thenReturn(expectedAuthors);
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/createbook"))
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("allAuthors", expectedAuthors))
                .andExpect(model().attribute("allGenres", expectedGenres));
    }

    @DisplayName("должен отдавать страницу редактирования книги с правильными представлением и моделью данных ")
    @Test
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        var expectedBook = dtoBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedAuthors = dtoAuthors;
        var expectedGenres = dtoGenres;
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(expectedBook));
        when(authorService.findAll()).thenReturn(expectedAuthors);
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/editbook/" + FIRST_BOOK_ID))
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("allAuthors", expectedAuthors))
                .andExpect(model().attribute("allGenres", expectedGenres));
    }

    @DisplayName("должен сохранять новую книгу и перенаправлять на верный url ")
    @Test
    void shouldSaveNewBookAndRedirectToContextPath() throws Exception {
        var expectedNewBook = new BookDto(0, "newBook", dtoAuthors.get(0), dtoGenres.subList(0, 2));
        var expectedGenresIds = expectedNewBook.genres().stream().map(GenreDto::id).collect(Collectors.toSet());
        var postMock = post("/savebook")
                .param("id", String.valueOf(expectedNewBook.id()))
                .param("title", expectedNewBook.title())
                .param("author.id", String.valueOf(expectedNewBook.author().id()));
        expectedNewBook.genres().forEach(
                genreDto -> postMock.param("genres", String.valueOf(genreDto.id())));

        mvc.perform(postMock).andExpect(view().name("redirect:/"));
        verify(bookService, times(1))
                .save(expectedNewBook.id(), expectedNewBook.title(), expectedNewBook.author().id(), expectedGenresIds);
    }

    @DisplayName("должен сохранять изменения в текущей книге и перенаправлять на верный url ")
    @Test
    void shouldSaveCurrentBookAndRedirectToContextPath() throws Exception {
        var expectedCurrentBook = dtoBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedGenresIds = expectedCurrentBook.genres().stream().map(GenreDto::id).collect(Collectors.toSet());

        var postMock = post("/savebook")
                .param("id", String.valueOf(expectedCurrentBook.id()))
                .param("title", expectedCurrentBook.title())
                .param("author.id", String.valueOf(expectedCurrentBook.author().id()));
        expectedCurrentBook.genres().forEach(
                genreDto -> postMock.param("genres", String.valueOf(genreDto.id())));

        mvc.perform(postMock).andExpect(view().name("redirect:/"));
        verify(bookService, times(1))
                .save(expectedCurrentBook.id(), expectedCurrentBook.title(), expectedCurrentBook.author().id(), expectedGenresIds);
    }

    @DisplayName("должен удалять книгу и перенаправлять на верный url ")
    @Test
    void shouldDeleteBookAndRedirectToContextPath() throws Exception {
        mvc.perform(post("/deletebook/{id}", FIRST_BOOK_ID))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(FIRST_BOOK_ID);
    }

    @DisplayName("должен отдавать страницу ошибки ")
    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        doThrow(new BookNotFoundException("Book with id %d not found".formatted(FIRST_BOOK_ID)))
                .when(bookService).deleteById(FIRST_BOOK_ID);
        mvc.perform(post("/deletebook/{id}", FIRST_BOOK_ID))
                .andExpect(view().name("customError"));
    }

}
