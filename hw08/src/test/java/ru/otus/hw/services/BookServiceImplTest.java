package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с книгами в виде dto ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({BookServiceImpl.class, BookConverter.class,
        AuthorConverter.class, GenreConverter.class,
        AuthorRepositoryImpl.class, GenreRepositoryImpl.class})
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private MongoOperations mongoOperations;

    private List<AuthorDto> dtoAuthors;

    private List<GenreDto> dtoGenres;

    private List<BookDto> dtoBooks;

    private static final long FIRST_BOOK_ID = 1L;

    private static final long SECOND_BOOK_ID = 2L;

    private String insertBookId;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
        dtoBooks = TestDataGenerator.generateExpectedDtoBooks(dtoAuthors, dtoGenres);
    }

    @DisplayName("должен загружать книгу по id в виде dto ")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = dtoBooks.get((int) FIRST_BOOK_ID - 1);
        var actualBook = bookService.findById(String.valueOf(FIRST_BOOK_ID));

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
                .matches(book -> book.id() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        mongoOperations.remove(
                new Query(Criteria.where("_id").is(returnedBook.id())), Book.class);
    }

    @DisplayName("должен сохранять измененную книгу и возвращать dto ")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new BookDto(String.valueOf(SECOND_BOOK_ID), "BookTitle_10500", dtoAuthors.get(2),
                List.of(dtoGenres.get(4), dtoGenres.get(5)));
        var returnedBook = bookService.update(String.valueOf(SECOND_BOOK_ID), "BookTitle_10500", dtoAuthors.get(2).id(),
                Set.of(dtoGenres.get(4).id(), dtoGenres.get(5).id()));

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.id() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        bookService.update(String.valueOf(SECOND_BOOK_ID), "BookTitle_2", dtoAuthors.get(1).id(),
                Set.of(dtoGenres.get(2).id(), dtoGenres.get(3).id()));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var expectedBook = mongoOperations.insert(
                new Book(null, "BookTitleForDellete",
                        new Author("2", "Author_2"),
                        List.of(new Genre("3", "Genre_3"), new Genre("4", "Genre_4"))));
        var expectedComment = mongoOperations.insert(
                new Comment(null, "CommentForDelete", expectedBook));

        assertThat(mongoOperations.findById(expectedBook.getId(), Book.class)).isNotNull();
        assertThat(mongoOperations.findById(expectedComment.getId(), Comment.class)).isNotNull();
        bookService.deleteById(expectedBook.getId());
        assertThat(mongoOperations.findById(expectedBook.getId(), Book.class)).isNull();
        assertThat(mongoOperations.findById(expectedComment.getId(), Comment.class)).isNull();
    }
}
