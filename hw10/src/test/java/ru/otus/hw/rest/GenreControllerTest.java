package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.rest.exceptions.GenreNotFoundException;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с жанрами в формате json ")
@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private GenreServiceImpl genreService;

    private List<GenreDto> dtoGenres;

    @BeforeEach
    void setUp() {
        dtoGenres = TestDataGenerator.generateExpectedDtoGenres();
    }

    @DisplayName("должен возвращать корректный список жанров в формате json ")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        given(genreService.findAll()).willReturn(dtoGenres);
        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoGenres)));
    }

    @DisplayName("должен возвращать заданную ошибку, если жанры не найдены ")
    @Test
    void shouldReturnExpectedErrorWhenGenresNotFound() throws Exception {
        String expectedErrorText = "Genre not found";

        given(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).willReturn(expectedErrorText);
        given(genreService.findAll()).willThrow(new GenreNotFoundException("Error"));
        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedErrorText));
    }

}
