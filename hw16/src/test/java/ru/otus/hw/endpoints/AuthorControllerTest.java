package ru.otus.hw.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с авторами в формате json ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private AuthorRepository authorRepository;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @BeforeEach
    void setUp() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/$", "");
    }

    @DisplayName("должен возвращать корректный список авторов в формате json ")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        Page<Author> mockPage = new PageImpl<>(List.of(new Author(1L, "Test Author")));
        doReturn(mockPage).when(authorRepository).findAll(any(Pageable.class));

        mvc.perform(get("%s/authors".formatted(dataRestBasePath)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.authors", hasSize(1)));
        verify(authorRepository, times(1)).findAll(any(Pageable.class));
    }

}
