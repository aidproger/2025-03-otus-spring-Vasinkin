package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.DepartmentType;

public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Long> {
}
