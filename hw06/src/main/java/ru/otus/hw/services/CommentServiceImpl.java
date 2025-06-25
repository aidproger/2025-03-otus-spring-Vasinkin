package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long id) {
        var comments = commentRepository.findAllByBookId(id);
        if (comments.isEmpty()) {
            return List.of();
        }
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();
    }

    @Transactional
    @Override
    public CommentDto insert(String text, long bookId) {
        var comment = save(0, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text, long bookId) {
        var comment = save(id, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
        commentRepository.delete(comment.get());
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

}
