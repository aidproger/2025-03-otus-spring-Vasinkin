package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.AuthorDto;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.id(), author.fullName());
    }
}
