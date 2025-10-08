package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        List<BookDto> books = bookService.findAll();
        if (books.isEmpty()) {
            throw new BookNotFoundException();
        }
        return books;
    }

    @GetMapping("/api/v1/book/{id}")
    public BookDto getBookById(@PathVariable("id") long id) {
        BookDto book = bookService.findById(id)
                .orElseThrow(BookNotFoundException::new);//переделать исключение
        return book;
    }

    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/savebook")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        var genresIds = bookDto.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());
        BookDto insertedBook = bookService.insert(bookDto.title(), bookDto.author().id(), genresIds);
        return ResponseEntity.ok(insertedBook);
    }

    @PutMapping("/api/v1/savebook")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        var genresIds = bookDto.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet());
        BookDto insertedBook = bookService.update(bookDto.id(), bookDto.title(), bookDto.author().id(), genresIds);
        return ResponseEntity.ok(insertedBook);
    }

}
