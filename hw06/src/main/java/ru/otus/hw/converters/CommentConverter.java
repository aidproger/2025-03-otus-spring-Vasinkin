package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDto comment) {
        return "Id: %d, Text: %s, Book: {%s}".
                formatted(comment.id(), comment.text(), bookConverter.bookToString(comment.book()));
    }
}
