package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.exceptions.DocumentNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAll() {
        var comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            return List.of();
        }
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(String id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Book with id %s not found".formatted(id)));
        var comments = commentRepository.findAllByBookId(id);
        if (comments.isEmpty()) {
            return List.of();
        }
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(
                comment -> new CommentDto(comment.getId(), comment.getText()));
    }

    @Transactional
    @Override
    public CommentDto insert(String text, String bookId) {
        var comment = save(null, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public CommentDto update(String id, String text, String bookId) {
        var comment = save(id, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        var comment = commentRepository.findById(id).
                orElseThrow(() -> new DocumentNotFoundException("Comment with id %s not found".formatted(id)));
        commentRepository.delete(comment);
    }

    private Comment save(String id, String text, String bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new DocumentNotFoundException("Book with id %s not found".formatted(bookId)));

        var comment = new Comment(id, text, bookId);
        return commentRepository.save(comment);
    }

}
