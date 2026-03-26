package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Well;

import java.util.List;
import java.util.Optional;

public interface WellRepository extends JpaRepository<Well, Long> {

    @EntityGraph(attributePaths = "department")
    @Override
    List<Well> findAll();

    @EntityGraph(attributePaths = "department")
    @Override
    Optional<Well> findById(Long id);

}
