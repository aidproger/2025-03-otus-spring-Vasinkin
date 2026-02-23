package ru.otus.hw.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author"})
    @Transactional(readOnly = true)
    @Override
    List<Book> findAll();

    @EntityGraph(attributePaths = {"author"})
    @Transactional(readOnly = true)
    @Override
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genres"})
    @Transactional(readOnly = true)
    @Override
    Optional<Book> findById(Long id);
}
