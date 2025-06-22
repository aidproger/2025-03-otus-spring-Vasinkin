package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.domain.CommentDto;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long id) {
        var comments = commentRepository.findAllByBookId(id);
        if (comments.isEmpty()) {
            return List.of();
        }
        var book = bookConverter.convertEntityToDto(comments.get(0).getBook());
        return comments.stream()
                .map(c -> new CommentDto(c.getId(), c.getText(), book))
                .toList();
    }

}
