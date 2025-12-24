package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/book/*").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/*/books").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/*/books/*").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/*/authors").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/*/genres").hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/*/books/*/comments")
                            .hasAnyRole("READ", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/createbook").hasAnyRole("BOOK_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/*/books").hasAnyRole("BOOK_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/createcomment").hasAnyRole("COMMENT_ADD", "ACL_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/*/books/*/comments")
                            .hasAnyRole("COMMENT_ADD", "ACL_ADMIN")
                        .anyRequest().authenticated()
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
