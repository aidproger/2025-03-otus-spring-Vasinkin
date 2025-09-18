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
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/createcomment")
    public String viewCreatingCommentForBookId(@RequestParam("bookid") long bookid, Model model) {
        var book = bookService.findById(bookid)
                .orElseThrow(BookNotFoundException::new);
        model.addAttribute("book", book);
        model.addAttribute("comment", new CommentDto(0, ""));
        return "createComment";
    }

    @PostMapping("/editcomment")
    public String saveBook(@Valid @ModelAttribute("comment") CommentDto comment,
                           BindingResult bindingResult,
                           @RequestParam("bookid") long bookid) {
        if (bindingResult.hasErrors()) {
            return "createComment";
        }

        commentService.insert(comment.text(), bookid);//проверка на NULL

        return "redirect:/book/" + bookid;
    }

    @PostMapping("/deletecomment/{id}")
    public String deleteBookById(@PathVariable("id") long id, @RequestParam("bookid") long bookid) {
        commentService.deleteById(id);
        return "redirect:/book/" + bookid;
    }
}
