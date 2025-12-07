package ru.otus.hw.pages;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentPageController {

    private final Logger log = LoggerFactory.getLogger(CommentPageController.class);

    @GetMapping("/createcomment")
    public String viewCreatingCommentForBookId(@RequestParam("bookId") long bookId, Model model) {
        log.info("Request view creating with request param bookId:{}", bookId);
        model.addAttribute("bookId", bookId);
        return "createComment";
    }
}
