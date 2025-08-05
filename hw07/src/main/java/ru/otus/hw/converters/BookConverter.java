package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        var genresString = book.genres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.id(),
                book.title(),
                authorConverter.authorToString(book.author()),
                genresString);
    }

    public BookDto convertEntityToDto(Book book) {
        var genres = book.getGenres().stream()
                .map(g -> new GenreDto(g.getId(), g.getName()))
                .toList();
        var author = new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName());
        return new BookDto(book.getId(), book.getTitle(), author, genres);
    }
}
