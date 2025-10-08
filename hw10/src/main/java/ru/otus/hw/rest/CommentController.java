package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.rest.exceptions.CommentNotFoundException;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/comments")
    public List<CommentDto> getAllCommentsByBookId(@RequestParam("bookId") long bookId) {
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comments;
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/comment")
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,
                                                 @RequestParam("bookId") long bookId) {
        var savedComment = commentService.insert(commentDto.text(), bookId);
        return ResponseEntity.ok(savedComment);
    }

}
