package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public List<CommentDto> getAllCommentsByBookId(@PathVariable("bookId") long bookId) {
        log.info("Request all data with path variable bookId:{}", bookId);
        return commentService.findAllByBookId(bookId);
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("id") long id) {
        log.info("Request delete with path variable id:{}", id);
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,
                                                 @PathVariable("bookId") long bookId) {
        log.info("Request add with request body commentDto:{} and path variable bookId:{}", commentDto, bookId);
        var savedComment = commentService.insert(commentDto.text(), bookId);
        return ResponseEntity.ok(savedComment);
    }

}
