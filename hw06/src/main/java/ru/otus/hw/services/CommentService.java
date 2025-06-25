package ru.otus.hw.services;

import ru.otus.hw.domain.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findAllByBookId(Long id);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text, long bookId);

    void deleteById(long id);
}
