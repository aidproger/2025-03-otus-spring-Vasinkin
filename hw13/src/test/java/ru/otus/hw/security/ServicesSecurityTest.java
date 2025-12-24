package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.security.services.AuthorizeUserDetailsService;
import ru.otus.hw.services.acl.AclServiceWrapperService;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис проверки конфигурации безопасности для всех методов ")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@SpringBootTest
public class ServicesSecurityTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AuthorizeUserDetailsService userDetailsService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AclServiceWrapperService aclServiceWrapperService;

    @MockBean
    private BookConverter bookConverter;

    @BeforeEach
    void setUp() {
        given(bookRepository.findById(anyLong())).willReturn(TestDataGenerator.generateOptionalEmptyBook());
        given(commentRepository.save(any(Comment.class))).willReturn(TestDataGenerator.generateEmptyComment());
        given(commentRepository.findById(anyLong())).willReturn(TestDataGenerator.generateOptionalEmptyComment());
        given(authorRepository.findById(anyLong())).willReturn(TestDataGenerator.generateOptionalEmptyAuthor());
        given(genreRepository.findAllById(any(Set.class))).willReturn(TestDataGenerator.generateListEmptyGenres());
    }

    @DisplayName("должен проверять привелегии доступа к методам сервисов ")
    @ParameterizedTest(name = "{2} {3} для пользователя {0} доступ должен быть запрёщён: {1} (true - да, false - нет)")
    @MethodSource("ru.otus.hw.common.AclSecurityTestDataGenerator#getAclSecurityTestData")
    void shouldCheckCorrectAuthorityStatus(String username, boolean accessDenied, String className,
                                           String methodName, Class<?>[] argsClazz, Object[] args) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Class<?> clazz = Class.forName(className);
        Object service = context.getBean(clazz);

        Method method = clazz.getMethod(methodName, argsClazz);

        if (accessDenied) {
            assertThatThrownBy(() -> method.invoke(service, args)).hasCauseInstanceOf(AccessDeniedException.class);
        } else {
            assertThatCode(() -> method.invoke(service, args)).doesNotThrowAnyException();
        }

    }

}
