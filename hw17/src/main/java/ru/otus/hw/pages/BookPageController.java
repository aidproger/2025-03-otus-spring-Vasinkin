package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final Logger log = LoggerFactory.getLogger(BookPageController.class);

    @GetMapping("/")
    public String viewAllBooks() {
        log.info("Request view all");
        return "listBook";
    }

    @GetMapping("/book/{id}")
    public String viewBookById(@PathVariable("id") long id, Model model) {
        log.info("Request view with path variable id:{}", id);
        model.addAttribute("id", id);
        return "book";
    }

    @GetMapping("/createbook")
    public String viewCreatingBook(Model model) {
        log.info("Request view creating");
        model.addAttribute("id", 0);
        return "createBook";
    }

    @GetMapping("/editbook/{id}")
    public String viewEditBook(@PathVariable("id") long id, Model model) {
        log.info("Request view edit with path variable id:{}", id);
        model.addAttribute("id", id);
        return "createBook";
    }
}
