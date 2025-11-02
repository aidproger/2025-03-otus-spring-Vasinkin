package ru.otus.hw.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Контроллер взаимодействия с книгами через html страницы ")
@WebMvcTest(controllers = BookPageController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookPageControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final long FIRST_BOOK_ID = 1;

    @DisplayName("должен отдавать корневую страницу отображения книг, авторов и жанров с правильным представлением ")
    @Test
    void shouldRenderBooksListPageWithCorrectView() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("listBook"));
    }

    @DisplayName("должен отдавать страницу отображения книги с комментариями с правильными представлением и моделью данных ")
    @Test
    void shouldRenderBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/book/{id}", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("id", FIRST_BOOK_ID));
    }

    @DisplayName("должен отдавать страницу создания книги с правильными представлением и моделью данных ")
    @Test
    void shouldRenderCreateBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/createbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("id", 0));
    }

    @DisplayName("должен отдавать страницу редактирования книги с правильными представлением и моделью данных ")
    @Test
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/editbook/{id}", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("id", FIRST_BOOK_ID));
    }

}
