package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {

    public BookDto convertEntityToDto(Book book) {
        var genres = book.getGenres().stream()
                .map(g -> new GenreDto(g.getId(), g.getName()))
                .toList();
        var author = new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName());
        return new BookDto(book.getId(), book.getTitle(), author, genres);
    }
}
