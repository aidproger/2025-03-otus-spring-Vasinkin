package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.pages.BookPageController;
import ru.otus.hw.pages.CommentPageController;
import ru.otus.hw.pages.LoginPageController;
import ru.otus.hw.rest.AuthorController;
import ru.otus.hw.rest.BookController;
import ru.otus.hw.rest.CommentController;
import ru.otus.hw.rest.GenreController;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер проверки конфигурации безопасности для всех endpoints ")
@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = {CommentPageController.class,
        BookPageController.class, LoginPageController.class,
        AuthorController.class, GenreController.class,
        CommentController.class, BookController.class})
public class ControllersSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private BookServiceImpl bookService;

    private static Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap;

    @BeforeAll
    static void initialize() {
        methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete", MockMvcRequestBuilders::delete);
    }

    @DisplayName("должен возвращать ожидаемый http статус ")
    @ParameterizedTest(name = "{0} {1} для пользователя {2} должен вернуться {3} http статус")
    @MethodSource("ru.otus.hw.common.SecurityTestDataGenerator#getSecurityTestData")
    void shouldReturnExpectedStatus(String method, String url,
                                    String userName, int status,
                                    boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url);

        if (Objects.nonNull(userName)) {
            request = request.with(user(userName));
        }
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().is(status));

        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        return methodMap.get(method).apply(url);
    }

}
