package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.exceptions.AuthorNotFoundException;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.rest.exceptions.GenreNotFoundException;
import ru.otus.hw.services.acl.AclServiceWrapperService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookConverter::convertEntityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
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
        var book = save(0, title, authorId, genresIds);
        aclServiceWrapperService.createPermissions(book, Set.of(BasePermission.WRITE, BasePermission.DELETE));

        return bookConverter.convertEntityToDto(book);
    }

    @PreAuthorize("canWrite(#id, T(ru.otus.hw.models.Book))")
    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        var book = save(id, title, authorId, genresIds);
        return bookConverter.convertEntityToDto(book);
    }

    @PreAuthorize("canDelete(#id, T(ru.otus.hw.models.Book))")
    @Transactional
    @Override
    public void deleteById(long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id %d not found".formatted(id)));
        bookRepository.delete(book);

        aclServiceWrapperService.deleteAllPermissions(book);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new GenreNotFoundException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new GenreNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }

}
