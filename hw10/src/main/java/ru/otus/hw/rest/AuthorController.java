package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.rest.exceptions.AuthorNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authors = authorService.findAll();
        if (authors.isEmpty()) {
            throw new AuthorNotFoundException();
        }
        return authors;
    }

}
