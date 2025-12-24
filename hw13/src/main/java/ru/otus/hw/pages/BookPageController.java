package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/")
    public String viewAllBooks() {
        return "listBook";
    }

    @GetMapping("/book/{id}")
    public String viewBookById(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "book";
    }

    @GetMapping("/createbook")
    public String viewCreatingBook(Model model) {
        model.addAttribute("id", 0);
        return "createBook";
    }

    @GetMapping("/editbook/{id}")
    public String viewEditBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "createBook";
    }
}
