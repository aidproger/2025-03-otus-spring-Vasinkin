package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.List;
import java.util.Locale;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер взаимодействия с авторами в формате json ")
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private AuthorServiceImpl authorService;

    private List<AuthorDto> dtoAuthors;

    @BeforeEach
    void setUp() {
        dtoAuthors = TestDataGenerator.generateExpectedDtoAuthors();
    }

    @DisplayName("должен возвращать корректный список авторов в формате json ")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        given(authorService.findAll()).willReturn(dtoAuthors);
        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoAuthors)));
    }

    @DisplayName("должен возвращать заданную ошибку, если авторры не найдены ")
    @Test
    void shouldReturnExpectedErrorWhenAuthorsNotFound() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        var expectedErrorText = messageSource.getMessage("author-not-found-error", null,
                LocaleContextHolder.getLocale());

        given(authorService.findAll()).willReturn(List.of());
        mvc.perform(get("/api/v1/authors")
                        .locale(Locale.ENGLISH))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedErrorText));
    }

}
