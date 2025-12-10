package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        log.info("Request all data");
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto getBookById(@PathVariable("id") long id) {
        log.info("Request data with path variable id:{}", id);
        BookDto book = bookService.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return book;
    }

    @DeleteMapping("/api/v1/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") long id) {
        log.info("Request delete with path variable id:{}", id);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/books")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        log.info("Request add with request body bookDto:{}", bookDto);
        var genresIds = bookDto.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());
        BookDto insertedBook = bookService.insert(bookDto.title(), bookDto.author().id(), genresIds);
        return ResponseEntity.ok(insertedBook);
    }

    @PutMapping("/api/v1/books")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        log.info("Request update with request body bookDto:{}", bookDto);
        var genresIds = bookDto.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());
        BookDto insertedBook = bookService.update(bookDto.id(), bookDto.title(), bookDto.author().id(), genresIds);
        return ResponseEntity.ok(insertedBook);
    }

}
