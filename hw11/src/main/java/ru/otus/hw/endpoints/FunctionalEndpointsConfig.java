package ru.otus.hw.endpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalEndpointsConfig {

    @Bean
    public RouterFunction<ServerResponse> authorRoutes(AuthorRepository authorRepository) {
        return route()
                .GET("/func/api/v1/authors",
                        request ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(authorRepository.findAll(), Author.class))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> genreRoutes(GenreRepository genreRepository) {
        return route()
                .GET("/func/api/v1/genres",
                        request ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(genreRepository.findAll(), Genre.class))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(BookRepository bookRepository,
                                                     AuthorRepository authorRepository,
                                                     GenreRepository genreRepository,
                                                     CommentRepository commentRepository) {
        return route()
                .GET("/func/api/v1/books/{id}",
                        request ->
                                bookRepository.findById(request.pathVariable("id"))
                                        .flatMap(book -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(fromValue(book)))
                                        .switchIfEmpty(ServerResponse.notFound().build()))
                .GET("/func/api/v1/books",
                        request ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(bookRepository.findAll(), Book.class))
                .DELETE("/func/api/v1/books/{id}",
                        request ->
                                bookRepository.existsById(request.pathVariable("id"))
                                        .flatMap(exists ->
                                                exists ? bookRepository.deleteById(request.pathVariable("id"))
                                                        .then(commentRepository.deleteAllByBookId(
                                                                request.pathVariable("id")))
                                                        .then(ServerResponse.noContent().build())
                                                        : ServerResponse.notFound().build()))
                .POST("/func/api/v1/books",
                        request -> request.bodyToMono(Book.class)
                                .flatMap(book ->
                                        Mono.zip(
                                                authorRepository.findById(book.getAuthor().getId()),
                                                genreRepository.findAllByIds(
                                                        book.getGenres()
                                                                .stream().map(Genre::getId)
                                                                .collect(Collectors.toSet())),
                                                (author, genres) -> {
                                                    book.setAuthor(author);
                                                    book.setGenres(genres);
                                                    return book;
                                                }))
                                .flatMap(bookRepository::save)
                                .flatMap(savedBook -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromValue(savedBook))))
                .PUT("/func/api/v1/books",
                        request ->
                                request.bodyToMono(Book.class)
                                        .flatMap(book -> bookRepository.existsById(book.getId())
                                                .flatMap(exists ->
                                                        exists ? bookRepository.save(book)
                                                                .flatMap(savedBook -> ServerResponse.ok()
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .body(fromValue(savedBook)))
                                                                : ServerResponse.notFound().build())))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> commentRoutes(CommentRepository commentRepository,
                                                        BookRepository bookRepository) {
        return route()
                .GET("/func/api/v1/books/{bookId}/comments",
                        request ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(commentRepository.findAllByBookId(
                                                request.pathVariable("bookId")), Comment.class))
                .DELETE("/func/api/v1/books/{bookId}/comments/{id}",
                        request ->
                                commentRepository.existsById(request.pathVariable("id"))
                                        .flatMap(exists ->
                                                exists ? commentRepository.deleteById(request.pathVariable("id"))
                                                        .then(ServerResponse.noContent().build())
                                                        : ServerResponse.notFound().build()))
                .POST("/func/api/v1/books/{bookId}/comments",
                        request -> Mono.zip(
                                        bookRepository.findById(request.pathVariable("bookId")),
                                        request.bodyToMono(Comment.class),
                                        (book, comment) -> {
                                            comment.setBook(book);
                                            return comment;
                                        })
                                .flatMap(commentRepository::save)
                                .flatMap(savedComment -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromValue(savedComment)))
                                .switchIfEmpty(ServerResponse.notFound().build()))
                .build();
    }

}
