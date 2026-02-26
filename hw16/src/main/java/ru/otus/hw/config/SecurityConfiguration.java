package ru.otus.hw.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] PUBLIC_ENDPOINTS = {"/actuator/**", "/login", "/css/**", "/img/**", "/js/**"};

    private static final String[] BOOKS_REST_GET_ENDPOINTS = {"%s/books", "%s/books/*/author",
            "%s/books/*/genres", "%s/books/*", "%s/books/*/comments",};

    private static final String[] BOOKS_PAGES_GET_ENDPOINTS = {"/", "/book/*"};

    private static final String[] DELETE_REST_ENDPOINTS = {"%s/books/*", "%s/comments/*"};

    private String[] booksRestGetEndpoints;

    private String[] deleteRestEndpoints;

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @PostConstruct
    private void initDataRestBasePath() {
        dataRestBasePath = dataRestBasePath.replaceFirst("/v\\d+/", "/v*");
        booksRestGetEndpoints = Arrays.stream(BOOKS_REST_GET_ENDPOINTS).map(endpoint ->
                endpoint.formatted(dataRestBasePath)).toArray(String[]::new);
        deleteRestEndpoints = Arrays.stream(DELETE_REST_ENDPOINTS).map(endpoint ->
                endpoint.formatted(dataRestBasePath)).toArray(String[]::new);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, BOOKS_PAGES_GET_ENDPOINTS).hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, booksRestGetEndpoints).hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "%s/authors".formatted(dataRestBasePath))
                        .hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "%s/genres".formatted(dataRestBasePath))
                        .hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/createbook").hasAnyRole("BOOK_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.POST, "%s/books".formatted(dataRestBasePath))
                        .hasAnyRole("BOOK_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/createcomment").hasAnyRole("COMMENT_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.POST, "%s/comments".formatted(dataRestBasePath))
                        .hasAnyRole("COMMENT_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/editbook/*").authenticated()//ACL
                        .requestMatchers(HttpMethod.DELETE, deleteRestEndpoints).authenticated()//ACL
                        .anyRequest().denyAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
