package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class UserPageController {

    private final Logger log = LoggerFactory.getLogger(UserPageController.class);

    @GetMapping("/admin")
    public String viewAllUsers() {
        log.info("Admin request view all");
        return "users";
    }

    @GetMapping("/admin/user/{id}")
    public String viewAddAndEditBook(@PathVariable("id") long id, Model model) {
        log.info("Request view creating or edit with path variable id:{}", id);
        model.addAttribute("id", id);
        return "user";
    }

}
