package ru.otus.hw.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@SpringBootTest
public class FunctionalEndpointsConfigTest {

    @Autowired
    @Qualifier("authorRoutes")
    private RouterFunction<ServerResponse> authorRoutes;

    @Autowired
    @Qualifier("genreRoutes")
    private RouterFunction<ServerResponse> genreRoutes;

    @Autowired
    @Qualifier("bookRoutes")
    private RouterFunction<ServerResponse> bookRoutes;

    @Autowired
    @Qualifier("commentRoutes")
    private RouterFunction<ServerResponse> commentRoutes;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    private static final long FIRST_BOOK_ID = 1L;

    private static final long SECOND_COMMENT_ID = 2L;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataGenerator.generateExpectedDbAuthors();
        dbGenres = TestDataGenerator.generateExpectedDbGenres();
        dbBooks = TestDataGenerator.generateExpectedDbBooks();
        dbComments = TestDataGenerator.generateExpectedDbCommentsByBookId(String.valueOf(FIRST_BOOK_ID));
    }

    @DisplayName("должен возвращать корректный список авторов в формате json ")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {

        var authorsFlux = Flux.fromIterable(dbAuthors);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRoutes)
                .build();

        given(authorRepository.findAll()).willReturn(authorsFlux);

        client.get()
                .uri("/func/api/v1/authors")
                .exchange()
                .expectStatus()
                .isOk().expectBodyList(Author.class)
                .hasSize(dbAuthors.size());
    }

    @DisplayName("должен возвращать корректный список жанров в формате json ")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {

        var genresFlux = Flux.fromIterable(dbGenres);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRoutes)
                .build();

        given(genreRepository.findAll()).willReturn(genresFlux);

        client.get()
                .uri("/func/api/v1/genres")
                .exchange()
                .expectStatus()
                .isOk().expectBodyList(Genre.class)
                .hasSize(dbGenres.size());
    }

    @DisplayName("должен возвращать книгу по id в формате json ")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        var book = dbBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedBook = Mono.just(book);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        given(bookRepository.findById(String.valueOf(FIRST_BOOK_ID))).willReturn(expectedBook);

        client.get()
                .uri("/func/api/v1/books/%s".formatted(FIRST_BOOK_ID))
                .exchange()
                .expectStatus()
                .isOk().expectBody(Book.class)
                .isEqualTo(book);
    }

    @DisplayName("должен возвращать корректный список книг в формате json ")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {

        var booksFlux = Flux.fromIterable(dbBooks);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        given(bookRepository.findAll()).willReturn(booksFlux);

        client.get()
                .uri("/func/api/v1/books")
                .exchange()
                .expectStatus()
                .isOk().expectBodyList(Book.class)
                .hasSize(dbBooks.size());
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBookById() throws Exception {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        given(bookRepository.existsById(String.valueOf(FIRST_BOOK_ID))).willReturn(Mono.just(Boolean.TRUE));
        given(bookRepository.deleteById(String.valueOf(FIRST_BOOK_ID))).willReturn(Mono.empty());
        given(commentRepository.deleteAllByBookId(String.valueOf(FIRST_BOOK_ID))).willReturn(Mono.empty());

        client.delete()
                .uri("/func/api/v1/books/%s".formatted(FIRST_BOOK_ID))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @DisplayName("должен сохранять новую книгу и возвращать её в формате json ")
    @Test
    void shouldSaveNewBook() throws Exception {
        var book = dbBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedBook = Mono.just(book);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        given(authorRepository.findById(book.getAuthor().getId())).willReturn(Mono.just(book.getAuthor()));
        given(genreRepository.findAllByIds(book.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet())))
                .willReturn(Mono.just(book.getGenres()));
        given(bookRepository.save(book)).willReturn(expectedBook);

        client.post()
                .uri("/func/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(book))
                .exchange()
                .expectStatus()
                .isOk().expectBody(Book.class)
                .isEqualTo(book);
    }

    @DisplayName("должен сохранять измененную книгу и возвращать её в формате json ")
    @Test
    void shouldSaveUpdatedBook() throws Exception {
        var book = dbBooks.get((int) FIRST_BOOK_ID - 1);
        var expectedBook = Mono.just(book);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        given(bookRepository.existsById(book.getId())).willReturn(Mono.just(Boolean.TRUE));
        given(bookRepository.save(book)).willReturn(expectedBook);

        client.put()
                .uri("/func/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(book))
                .exchange()
                .expectStatus()
                .isOk().expectBody(Book.class)
                .isEqualTo(book);
    }


    @DisplayName("должен возвращать корректный список комментариев по id книги в формате json ")
    @Test
    void shouldReturnCorrectCommentsListByBookId() throws Exception {
        Flux<Comment> commentsFlux = Flux.fromIterable(dbComments);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentRoutes)
                .build();

        given(commentRepository.findAllByBookId(String.valueOf(FIRST_BOOK_ID))).willReturn(commentsFlux);

        client.get()
                .uri("/func/api/v1/books/%s/comments".formatted(FIRST_BOOK_ID))
                .exchange()
                .expectStatus()
                .isOk().expectBodyList(Comment.class)
                .hasSize(dbComments.size());
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteCommentById() throws Exception {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentRoutes)
                .build();

        given(commentRepository.existsById(String.valueOf(SECOND_COMMENT_ID))).willReturn(Mono.just(Boolean.TRUE));
        given(commentRepository.deleteById(String.valueOf(SECOND_COMMENT_ID))).willReturn(Mono.empty());

        client.delete()
                .uri("/func/api/v1/books/%s/comments/%s".formatted(FIRST_BOOK_ID, SECOND_COMMENT_ID))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @DisplayName("должен сохранять новый комметарий и возвращать его в формате json ")
    @Test
    void shouldSaveNewComment() throws Exception {
        var comment = dbComments.get((int) SECOND_COMMENT_ID - 1);
        var expectedComment = Mono.just(comment);

        var book = dbBooks.get((int) FIRST_BOOK_ID - 1);

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentRoutes)
                .build();

        given(bookRepository.findById(String.valueOf(FIRST_BOOK_ID))).willReturn(Mono.just(book));
        given(commentRepository.save(comment)).willReturn(expectedComment);

        client.post()
                .uri("/func/api/v1/books/%s/comments".formatted(FIRST_BOOK_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(comment))
                .exchange()
                .expectStatus()
                .isOk().expectBody(Comment.class)
                .isEqualTo(comment);
    }

}
