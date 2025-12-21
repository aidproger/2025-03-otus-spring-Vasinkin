package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.rest.exceptions.CommentNotFoundException;
import ru.otus.hw.services.acl.AclServiceWrapperService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    @PreAuthorize("canRead(T(ru.otus.hw.models.Comment))")
    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long id) {
        var comments = commentRepository.findAllByBookId(id);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();
    }

    @PreAuthorize("canRead(T(ru.otus.hw.models.Comment))")
    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(
                comment -> new CommentDto(comment.getId(), comment.getText()));
    }

    @PreAuthorize("canCreate(T(ru.otus.hw.models.Comment))")
    @Transactional
    @Override
    public CommentDto insert(String text, long bookId) {
        var comment = save(0, text, bookId);
        aclServiceWrapperService.createPermissions(comment, Set.of(BasePermission.WRITE, BasePermission.DELETE));

        return new CommentDto(comment.getId(), comment.getText());
    }

    @PreAuthorize("canWrite(#id, T(ru.otus.hw.models.Comment))")
    @Transactional
    @Override
    public CommentDto update(long id, String text, long bookId) {
        var comment = save(id, text, bookId);
        return new CommentDto(comment.getId(), comment.getText());
    }

    @PreAuthorize("canDelete(#id, T(ru.otus.hw.models.Comment))")
    @Transactional
    @Override
    public void deleteById(long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id %d not found".formatted(id)));
        commentRepository.delete(comment);

        aclServiceWrapperService.deleteAllPermissions(comment);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %d not found".formatted(bookId)));

        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

}
