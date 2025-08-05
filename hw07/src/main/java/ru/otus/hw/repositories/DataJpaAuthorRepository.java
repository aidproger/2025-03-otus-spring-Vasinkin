package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Author;

public interface DataJpaAuthorRepository extends JpaRepository<Author, Long> {

}
