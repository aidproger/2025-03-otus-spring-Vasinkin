package ru.otus.hw.services;

import ru.otus.hw.domain.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findAllByBookId(Long id);
}
