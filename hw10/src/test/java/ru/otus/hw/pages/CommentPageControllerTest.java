package ru.otus.hw.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер взаимодействия с комментариями через html страницы ")
@WebMvcTest(CommentPageController.class)
public class CommentPageControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final long FIRST_BOOK_ID = 1;

    @DisplayName("должен отдавать страницу создания комментария с правильными представлением и моделью данных ")
    @Test
    void shouldRenderCreatePageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/createcomment").param("bookId", String.valueOf(FIRST_BOOK_ID)))
                .andExpect(view().name("createComment"))
                .andExpect(model().attribute("bookId", FIRST_BOOK_ID));
    }

}
