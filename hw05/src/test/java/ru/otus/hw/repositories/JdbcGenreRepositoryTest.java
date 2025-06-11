package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import(JdbcGenreRepository.class)
public class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = TestDataGenerator.generateExpectedDbGenres();
    }

    @DisplayName("должен загружать список всех жанров ")
    @Test
    void shouldReturnCorrectGenreList() {
        var actualGenres = repositoryJdbc.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанры по ids ")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.common.TestDataGenerator#generateExpectedDbArraysGenres")
    void shouldReturnCorrectGenreById(List<Genre> expectedGenres) {
        var expectedIds = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = repositoryJdbc.findAllByIds(expectedIds);
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

}
