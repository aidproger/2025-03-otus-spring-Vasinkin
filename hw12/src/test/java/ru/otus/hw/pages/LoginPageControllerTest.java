package ru.otus.hw.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Контроллер аутентификации пользователя через html страницу ")
@WebMvcTest(controllers = LoginPageController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class LoginPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageSource messageSource;

    @DisplayName("должен отдавать страницу логина с правильным представлением ")
    @Test
    void shouldRenderLoginPageWithCorrectView() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @DisplayName("должен отдавать страницу логина с правильным представлением, моделью данных, текстом ошибки аутентификации ")
    @Test
    void shouldRenderLoginPageWithCorrectViewAndModelAttributesAndErrorText() throws Exception {
        String expectedErrorText = "Invalid login or password";

        given(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).willReturn(expectedErrorText);
        mvc.perform(get("/login")
                        .param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("errorText", expectedErrorText));
    }

}
