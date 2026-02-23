package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер проверки конфигурации безопасности для всех endpoints ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ControllersSecurityTest {

    @Autowired
    private MockMvc mvc;

    private static Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @BeforeAll
    static void initialize() {
        methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete", MockMvcRequestBuilders::delete);
    }

    @BeforeEach
    void setUp() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/$", "");
    }

    @DisplayName("должен возвращать ожидаемый http статус ")
    @ParameterizedTest(name = "{0} {1} для пользователя {2} с правами {3} должен вернуться {4} http статус")
    @MethodSource("ru.otus.hw.common.SecurityTestDataGenerator#getSecurityTestData")
    void shouldReturnExpectedStatus(String method, String url,
                                    String userName, String roles, int status,
                                    boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url.formatted(dataRestBasePath));

        if (Objects.nonNull(userName)) {
            UserRequestPostProcessor userRequestPostProcessor = user(userName);
            if (Objects.nonNull(roles)) {
                userRequestPostProcessor.roles(roles.split(";"));
            }
            request = request.with(userRequestPostProcessor);
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
