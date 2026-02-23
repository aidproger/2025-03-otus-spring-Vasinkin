package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    @Value("${spring.data.rest.basePath}")
    private String dataRestBasePath;

    @GetMapping("/")
    public String viewAllBooks(Model model) {
        model.addAttribute("dataRestBasePath", dataRestBasePath);
        return "listBook";
    }

    @GetMapping("/book/{id}")
    public String viewBookById(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("dataRestBasePath", dataRestBasePath);
        return "book";
    }

    @GetMapping("/createbook")
    public String viewCreatingBook(Model model) {
        model.addAttribute("id", 0);
        model.addAttribute("dataRestBasePath", dataRestBasePath);
        return "createBook";
    }

    @GetMapping("/editbook/{id}")
    public String viewEditBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("dataRestBasePath", dataRestBasePath);
        return "createBook";
    }
}
