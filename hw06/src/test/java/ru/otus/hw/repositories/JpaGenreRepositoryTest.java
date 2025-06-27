package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private static final int EXPECTED_NUMBER_OF_GENRES = 6;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = TestDataGenerator.generateExpectedDbGenres(em, EXPECTED_NUMBER_OF_GENRES);
    }

    @DisplayName("должен загружать список всех жанров ")
    @Test
    void shouldReturnCorrectGenreList() {
        var actualGenres = repositoryJpa.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанры по ids ")
    @Test
    void shouldReturnCorrectGenreListByIds() {
        var expectedGenres = dbGenres;
        var expectedIds = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = repositoryJpa.findAllByIds(expectedIds);
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }


}
