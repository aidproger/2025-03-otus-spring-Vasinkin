package ru.otus.hw.services;

import java.time.LocalDateTime;

public interface CommentService {

    boolean existsByCreationDateAfter(LocalDateTime after);
}
