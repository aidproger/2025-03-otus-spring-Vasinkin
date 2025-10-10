package ru.otus.hw.domain;

import org.hibernate.validator.constraints.Length;

public record CommentDto(long id, @Length(min = 3, max = 255) String text) {
}
