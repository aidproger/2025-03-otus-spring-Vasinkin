package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с книгами в виде dto ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({BookServiceImpl.class, BookConverter.class,
        AuthorConverter.class, GenreConverter.class})
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    private static final long SECOND_BOOK_ID = 2L;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
        dtoBooks = TestDataGenerator.generateExpectedDtoBooks(dtoAuthors, dtoGenres);
    }

    @DisplayName("должен загружать книгу по id в виде dto ")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = dtoBooks.get((int) SECOND_BOOK_ID - 1);
        var actualBook = bookService.findById(SECOND_BOOK_ID);

        assertThat(actualBook).isPresent()
                .get().isEqualTo(expectedBook);

        assertThat(actualBook.get().author())
                .isEqualTo(expectedBook.author());

        assertThat(actualBook.get().genres())
                .containsExactlyElementsOf(expectedBook.genres());
    }

    @DisplayName("должен загружать список всех книг в виде dto ")
    @Test
    void shouldReturnCorrectBooksListDto() {
        var expectedBooks = dtoBooks;
        var actualBooks = bookService.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу и возвращать dto ")
    @Test
    void shouldSaveNewBook() {
        var returnedBook = bookService.insert("BookTitle_10500", dtoAuthors.get(0).id(),
                Set.of(dtoGenres.get(0).id(), dtoGenres.get(2).id()));

        assertThat(returnedBook).isNotNull();
        var expectedBook = new BookDto(returnedBook.id(), "BookTitle_10500", dtoAuthors.get(0),
                List.of(dtoGenres.get(0), dtoGenres.get(2)));

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.id() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        bookService.deleteById(returnedBook.id());
    }

    @DisplayName("должен сохранять измененную книгу и возвращать dto ")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new BookDto(SECOND_BOOK_ID, "BookTitle_10500", dtoAuthors.get(2),
                List.of(dtoGenres.get(4), dtoGenres.get(5)));
        var returnedBook = bookService.update(SECOND_BOOK_ID, "BookTitle_10500", dtoAuthors.get(2).id(),
                Set.of(dtoGenres.get(4).id(), dtoGenres.get(5).id()));

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.id() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        bookService.update(SECOND_BOOK_ID, "BookTitle_2", dtoAuthors.get(1).id(),
                Set.of(dtoGenres.get(2).id(), dtoGenres.get(3).id()));
    }

}
