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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.handlers.BookRepositoryEventHandler;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

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

@DisplayName("Контроллер взаимодействия с книгами в формате json ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private BookRepository bookRepository;

    @MockBean
    private BookRepositoryEventHandler bookRepositoryEventHandler;

    private static final long SECOND_BOOK_ID = 2L;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @BeforeEach
    void setUp() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/$", "");
    }

    @DisplayName("должен возвращать корректный список книг в формате json ")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var returnBook = new Book(SECOND_BOOK_ID, "Book_1", new Author(), List.of(new Genre()),
                List.of(new Comment(1L, "Comment_1", null, new Book())));
        var mockPage = new PageImpl<>(List.of(returnBook));
        doReturn(mockPage).when(bookRepository).findAll(any(Pageable.class));

        mvc.perform(get("%s/books".formatted(dataRestBasePath)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.books", hasSize(1)));
    }

    @DisplayName("должен возвращать книгу по заданному идентификатору в формате json ")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        var returnBook = new Book(SECOND_BOOK_ID, "Book_1", new Author(), List.of(new Genre()),
                List.of(new Comment(1L, "Comment_1", null, new Book())));
        doReturn(Optional.of(returnBook)).when(bookRepository).findById(SECOND_BOOK_ID);

        mvc.perform(get("%s/books/{id}".formatted(dataRestBasePath), SECOND_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href")
                        .value(containsString("/books/%d".formatted(returnBook.getId()))));

        verify(bookRepository, times(1)).findById(SECOND_BOOK_ID);
    }

    @DisplayName("должен удалять книгу по идентификатору ")
    @Test
    void shouldCorrectlyDeleteBookById() throws Exception {
        doNothing().when(bookRepository).deleteById(any(Long.class));

        mvc.perform(delete("%s/books/{id}".formatted(dataRestBasePath), SECOND_BOOK_ID))
                .andExpect(status().isNoContent());

        verify(bookRepository, times(1)).deleteById(SECOND_BOOK_ID);
    }

    @DisplayName("должен добавлять книгу ")
    @Test
    void shouldCorrectlyAddBook() throws Exception {
        var expectedBook = new Book(0L, "Book_test", new Author(), List.of(new Genre()), List.of());
        var savedBook = new Book(10L, "Book_test", new Author(), List.of(new Genre()), List.of());
        var jsonBook = "{\"title\":\"%s\"}".formatted(expectedBook.getTitle());

        doReturn(savedBook).when(bookRepository).save(any(Book.class));
        mvc.perform(post("%s/books".formatted(dataRestBasePath))
                        .contentType(APPLICATION_JSON)
                        .content(jsonBook))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("books/%d".formatted(savedBook.getId()))));

        verify(bookRepository, times(1)).save(expectedBook);
    }

    @DisplayName("должен обновлять книгу ")
    @Test
    void shouldCorrectlyUpdateBook() throws Exception {
        var expectedBook = new Book(10L, "Book_test_before", new Author(), List.of(new Genre()), List.of());
        var savedBook = new Book(10L, "Book_test_after", new Author(), List.of(new Genre()), List.of());
        var jsonBook = "{\"id\":\"%s\", \"title\":\"%s\"}".formatted(expectedBook.getId(), expectedBook.getTitle());

        doReturn(savedBook).when(bookRepository).save(expectedBook);
        mvc.perform(post("%s/books".formatted(dataRestBasePath))
                        .contentType(APPLICATION_JSON)
                        .content(jsonBook))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("books/%d".formatted(savedBook.getId()))));

        verify(bookRepository, times(1)).save(expectedBook);
    }

}
