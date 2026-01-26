package ru.otus.hw.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var isAdmin = authentication.getAuthorities()
                .stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ACL_ADMIN"));

        if (isAdmin) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/");
        }
    }
}
