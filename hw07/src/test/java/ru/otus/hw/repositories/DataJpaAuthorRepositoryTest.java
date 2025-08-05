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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе DATA JPA для работы с авторами ")
@DataJpaTest
public class DataJpaAuthorRepositoryTest {
    @Autowired
    private DataJpaAuthorRepository repositoryDataJpa;

    @Autowired
    private TestEntityManager em;

    private static final long SECOND_AUTHOR_ID = 2L;

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataGenerator.generateExpectedDbAuthors(em, EXPECTED_NUMBER_OF_AUTHORS);
    }

    @DisplayName("должен загружать список всех авторов ")
    @Test
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = repositoryDataJpa.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора по id ")
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthor = em.find(Author.class, SECOND_AUTHOR_ID);
        var actualAuthor = repositoryDataJpa.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get().isEqualTo(expectedAuthor);
        System.out.println(actualAuthor);
    }

}
