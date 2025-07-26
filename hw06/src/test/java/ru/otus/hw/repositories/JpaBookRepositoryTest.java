package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import(JpaBookRepository.class)
public class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private static final long FIRST_BOOK_ID = 1L;

    private static final long SECOND_BOOK_ID = 2L;

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    private static final int EXPECTED_NUMBER_OF_GENRES = 6;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataGenerator.generateExpectedDbAuthors(em, EXPECTED_NUMBER_OF_AUTHORS);
        dbGenres = TestDataGenerator.generateExpectedDbGenres(em, EXPECTED_NUMBER_OF_GENRES);
        dbBooks = TestDataGenerator.generateExpectedDbBooks(em, EXPECTED_NUMBER_OF_BOOKS);
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = em.find(Book.class, SECOND_BOOK_ID);
        var actualBook = repositoryJpa.findById(SECOND_BOOK_ID);

        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var expectedBooks = dbBooks;
        var actualBooks = repositoryJpa.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);

    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(SECOND_BOOK_ID, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(em.find(Book.class, SECOND_BOOK_ID))
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var book = Optional.ofNullable(em.find(Book.class, FIRST_BOOK_ID));
        assertThat(book).isPresent();
        repositoryJpa.delete(book.get());

        book = Optional.ofNullable(em.find(Book.class, FIRST_BOOK_ID));
        assertThat(book).isEmpty();
    }

}
