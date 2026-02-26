package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional(readOnly = true)
    boolean existsByCreationDateAfter(LocalDateTime after);

    @EntityGraph(attributePaths = "book")
    @Transactional(readOnly = true)
    List<Comment> findAllByBookId(Long id);

    @EntityGraph(attributePaths = "book")
    @Transactional(readOnly = true)
    @Override
    Optional<Comment> findById(Long aLong);


}
