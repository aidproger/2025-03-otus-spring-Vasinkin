package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @EntityGraph(attributePaths = {"departmentType", "parent"})
    @Override
    List<Department> findAll();

    @EntityGraph(attributePaths = {"departmentType", "parent"})
    @Override
    Optional<Department> findById(Long id);

}
