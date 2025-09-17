package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.domain.BookDto;
import ru.otus.hw.domain.GenreDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String viewAllBooks(Model model) {
        var books = bookService.findAll();
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "listBook";
    }

    @GetMapping("/book/{id}")
    public String viewBookById(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id)
                .orElseThrow(BookNotFoundException::new);
        var comments = commentService.findAllByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }

    @GetMapping("/createbook")
    public String viewCreatingBook(Model model) {
        var book = new BookDto(0, "", new AuthorDto(0, ""), List.of());
        createBookModel(book, model);
        return "createBook";
    }

    @GetMapping("/editbook/{id}")
    public String viewEditBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id)
                .orElseThrow(BookNotFoundException::new);
        createBookModel(book, model);
        return "createBook";
    }

    @PostMapping("/savebook")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createBook";
        }
        var genresIds = book.genres().stream().map(GenreDto::id).collect(Collectors.toSet());
        bookService.save(book.id(), book.title(), book.author().id(), genresIds);
        return "redirect:/";
    }

    @PostMapping("/deletebook/{id}")
    public String deleteBookById(@PathVariable("id") long id) {
        try {
            bookService.deleteById(id);
        } catch (Exception e) {
            throw new BookNotFoundException();
        }
        return "redirect:/";
    }

    private void createBookModel(BookDto book, Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("allAuthors", authors);
        model.addAttribute("allGenres", genres);
    }
}
