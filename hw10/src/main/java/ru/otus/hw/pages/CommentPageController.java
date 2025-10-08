package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentPageController {

    @GetMapping("/createcomment")
    public String viewCreatingCommentForBookId(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "createComment";
    }
}
