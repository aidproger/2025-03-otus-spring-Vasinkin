package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.exceptions.BookNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        log.info("Find data by id:{}", id);
        return bookRepository.findById(id).map(bookConverter::convertEntityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        log.info("Find all data");
        var books = bookRepository.findAll().stream()
                .map(bookConverter::convertEntityToDto).toList();
        if (books.isEmpty()) {
            throw new BookNotFoundException();
        }
        return books;
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        log.info("Insert data title:{}, author id:{}, genres ids:{}", title, authorId, genresIds);
        var book = save(0, title, authorId, genresIds);
        return bookConverter.convertEntityToDto(book);
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        log.info("Update data by id:{}, title:{}, author id:{}, genres ids:{}", id, title, authorId, genresIds);
        var book = save(id, title, authorId, genresIds);
        return bookConverter.convertEntityToDto(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        log.info("Delete data by id:{}", id);
        var book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
        bookRepository.delete(book.get());
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }

}
