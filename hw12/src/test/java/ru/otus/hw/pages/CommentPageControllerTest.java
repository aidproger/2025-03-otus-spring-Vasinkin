package ru.otus.hw.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Контроллер взаимодействия с комментариями через html страницы с учётом аутентификации пользователя ")
@WebMvcTest(CommentPageController.class)
@Import(SecurityConfiguration.class)
public class CommentPageControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final long FIRST_BOOK_ID = 1;

    @DisplayName("должен отдавать страницу создания комментария с правильными представлением, моделью данных, для аутентифицированного пользователя ")
    @Test
    void shouldRenderCreatePageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/createcomment").with(user("login_1"))
                        .param("bookId", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("createComment"))
                .andExpect(model().attribute("bookId", FIRST_BOOK_ID));
    }

    @DisplayName("должен перенаправлять не аутентифицированного пользователя со страницы создания комментария на страницу логина ")
    @Test
    void shouldCorrectRedirectFromCreatePageToLoginPageForAnonymousUser() throws Exception {
        mvc.perform(get("/createcomment").with(anonymous())
                        .param("bookId", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(status().isFound());
    }

}
