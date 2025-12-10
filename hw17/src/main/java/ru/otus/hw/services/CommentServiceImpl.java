package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.exceptions.CommentNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long id) {
        log.info("Find all data by book id:{}", id);
        var comments = commentRepository.findAllByBookId(id);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();
    }

    @Override
    public Optional<CommentDto> findById(long id) {
        log.info("Find data by id:{}", id);
        return commentRepository.findById(id).map(
                comment -> new CommentDto(comment.getId(), comment.getText()));
    }

    @Transactional
    @Override
    public CommentDto insert(String text, long bookId) {
        log.info("Insert data by book id:{}, text:{}", bookId, text);
        var comment = save(0, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text, long bookId) {
        log.info("Update data by id:{}, book id:{}, text:{}", id, bookId, text);
        var comment = save(id, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        log.info("Delete data by id:{}", id);
        var comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException("Comment with id %d not found".formatted(id));
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
