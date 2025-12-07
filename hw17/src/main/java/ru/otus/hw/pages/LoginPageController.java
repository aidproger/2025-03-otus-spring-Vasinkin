package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class LoginPageController {

    private final Logger log = LoggerFactory.getLogger(LoginPageController.class);

    private final MessageSource messageSource;

    @GetMapping("/login")
    public String loginUser(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        log.info("Request with request param error:{}", error);
        if (error != null) {
            String errorText = messageSource.getMessage("login-error-login-and-password", null,
                    LocaleContextHolder.getLocale());
            model.addAttribute("errorText", errorText);
        }
        return "login";
    }
}
