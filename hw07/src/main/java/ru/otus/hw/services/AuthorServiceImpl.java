package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.AuthorDto;
import ru.otus.hw.repositories.DataJpaAuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final DataJpaAuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(a -> new AuthorDto(a.getId(), a.getFullName()))
                .toList();
    }
}
