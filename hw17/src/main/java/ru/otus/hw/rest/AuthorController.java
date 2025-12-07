package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getAllAuthors() {
        log.info("Request all data");
        return authorService.findAll();
    }

}
