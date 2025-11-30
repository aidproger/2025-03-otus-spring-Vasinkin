package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с авторами ")
@DataMongoTest
@Import(AuthorRepositoryImpl.class)
public class MongoDBAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private static final int SECOND_AUTHOR_INDEX = 1;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataGenerator.generateExpectedDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов ")
    @Test
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора по id ")
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthor = dbAuthors.get(SECOND_AUTHOR_INDEX);
        var actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get().isEqualTo(expectedAuthor);
        System.out.println(actualAuthor);
    }

}
