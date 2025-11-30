package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с жанрами в виде dto ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({GenreServiceImpl.class, GenreRepositoryImpl.class})
public class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    private List<GenreDto> dtoGenres;

    @BeforeEach
    void setUp() {
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
    }

    @DisplayName("должен загружать список всех жанров в виде dto ")
    @Test
    void shouldReturnCorrectGenresListDto() {
        var expectedGenres = dtoGenres;
        var actualGenres = genreService.findAll();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

}
