package ru.otus.hw.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с жанрами в формате json ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private GenreRepository genreRepository;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @BeforeEach
    void setUp() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/$", "");
    }

    @DisplayName("должен возвращать корректный список жанров в формате json ")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var mockPage = new PageImpl<>(List.of(new Genre(1L, "Test Genre")));
        doReturn(mockPage).when(genreRepository).findAll(any(Pageable.class));

        mvc.perform(get("%s/genres".formatted(dataRestBasePath)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.genres", hasSize(1)));
    }

}
