package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.services.security.AuthorizeUserDetailsService;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис проверки конфигурации acl безопасности для всех методов ")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@SpringBootTest
public class ServicesSecurityTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AuthorizeUserDetailsService userDetailsService;

    @DisplayName("должен проверять acl привелегии доступа к методам сервисов ")
    @ParameterizedTest(name = "{2} {3} для пользователя {0} доступ должен быть запрёщён: {1} (true - да, false - нет)")
    @MethodSource("ru.otus.hw.common.AclSecurityTestDataGenerator#getAclSecurityTestData")
    void shouldCheckCorrectAuthorityStatus(String username, boolean accessDenied, String className,
                                           String methodName, Class<?>[] argsClazz, Object[] args) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Class<?> clazz = Class.forName(className);
        Object handler = context.getBean(clazz);

        Method method = clazz.getMethod(methodName, argsClazz);

        if (accessDenied) {
            assertThatThrownBy(() -> method.invoke(handler, args)).hasCauseInstanceOf(AccessDeniedException.class);
        } else {
            assertThatCode(() -> method.invoke(handler, args)).doesNotThrowAnyException();
        }

    }

}
