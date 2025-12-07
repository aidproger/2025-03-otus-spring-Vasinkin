package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookConverter::convertDocumentToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::convertDocumentToDto).toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        var book = save(null, title, authorId, genresIds);
        return bookConverter.convertDocumentToDto(book);
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with id %s not found".formatted(id));
        }

        var bookSaved = save(id, title, authorId, genresIds);
        return bookConverter.convertDocumentToDto(bookSaved);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with id %s not found".formatted(id));
        }

        bookRepository.deleteById(id);
        commentRepository.deleteAllByBookId(id);
    }

    private Book save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new GenreNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }

}
