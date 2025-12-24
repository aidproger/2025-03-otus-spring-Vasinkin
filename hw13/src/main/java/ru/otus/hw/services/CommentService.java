package ru.otus.hw.services;

import ru.otus.hw.domain.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllByBookId(long id);

    Optional<CommentDto> findById(long id);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text, long bookId);

    void deleteById(long id);
}
