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
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.repositories.AuthorRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис взаимодействия с авторами в виде dto ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({AuthorServiceImpl.class, AuthorRepositoryImpl.class})
public class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    private List<AuthorDto> dtoAuthors;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
    }

    @DisplayName("должен загружать список всех авторов в виде dto ")
    @Test
    void shouldReturnCorrectAuthorsListDto() {
        var expectedAuthors = dtoAuthors;
        var actualAuthors = authorService.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

}
