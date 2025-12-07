package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.exceptions.AuthorNotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        log.info("Find all data");
        var authors = authorRepository.findAll().stream()
                .map(a -> new AuthorDto(a.getId(), a.getFullName()))
                .toList();
        if (authors.isEmpty()) {
            throw new AuthorNotFoundException();
        }
        return authors;
    }
}
