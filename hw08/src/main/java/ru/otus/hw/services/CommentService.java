package ru.otus.hw.services;

import ru.otus.hw.domain.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAll();

    List<CommentDto> findAllByBookId(String id);

    Optional<CommentDto> findById(String id);

    CommentDto insert(String text, String bookId);

    CommentDto update(String id, String text, String bookId);

    void deleteById(String id);
}
