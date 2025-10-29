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

@DisplayName("Контроллер взаимодействия с книгами через html страницы с учётом аутентификации пользователей ")
@WebMvcTest(BookPageController.class)
@Import(SecurityConfiguration.class)
public class BookPageControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final long FIRST_BOOK_ID = 1;

    @DisplayName("должен отдавать корневую страницу отображения книг, авторов и жанров с правильным представлением, для аутентифицированного пользователя ")
    @Test
    void shouldRenderBooksListPageWithCorrectView() throws Exception {
        mvc.perform(get("/").with(user("login_1")))
                .andExpect(status().isOk())
                .andExpect(view().name("listBook"));
    }

    @DisplayName("должен перенаправлять не аутентифицированного пользователя с корневого пути на страницу логина ")
    @Test
    void shouldCorrectRedirectFromBooksListPageToLoginPageForAnonymousUser() throws Exception {
        mvc.perform(get("/").with(anonymous()))
                .andExpect(status().isFound());
    }

    @DisplayName("должен отдавать страницу отображения книги с комментариями с правильными представлением и моделью данных, для аутентифицированного пользователя ")
    @Test
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/book/{id}", FIRST_BOOK_ID).with(user("login_1")))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("id", FIRST_BOOK_ID));
    }

    @DisplayName("должен перенаправлять не аутентифицированного пользователя со страницы отображения книги на страницу логина ")
    @Test
    void shouldCorrectRedirectFromBookPageToLoginPageForAnonymousUser() throws Exception {
        mvc.perform(get("/book/{id}", FIRST_BOOK_ID).with(anonymous()))
                .andExpect(status().isFound());
    }

    @DisplayName("должен отдавать страницу создания книги с правильными представлением и моделью данных, для аутентифицированного пользователя ")
    @Test
    void shouldRenderCreateBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/createbook").with(user("login_1")))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("id", 0));
    }

    @DisplayName("должен перенаправлять не аутентифицированного пользователя со страницы создания книги на страницу логина ")
    @Test
    void shouldCorrectRedirectFromCreateBookPageToLoginPageForAnonymousUser() throws Exception {
        mvc.perform(get("/createbook").with(anonymous()))
                .andExpect(status().isFound());
    }

    @DisplayName("должен отдавать страницу редактирования книги с правильными представлением и моделью данных, для аутентифицированного пользователя ")
    @Test
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/editbook/{id}", FIRST_BOOK_ID).with(user("login_1")))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("id", FIRST_BOOK_ID));
    }

    @DisplayName("должен перенаправлять не аутентифицированного пользователя со страницы редактирования книги на страницу логина ")
    @Test
    void shouldCorrectRedirectFromEditBookPageToLoginPageForAnonymousUser() throws Exception {
        mvc.perform(get("/editbook/{id}", FIRST_BOOK_ID).with(anonymous()))
                .andExpect(status().isFound());
    }

}
