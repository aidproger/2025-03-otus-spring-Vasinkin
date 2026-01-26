package ru.otus.hw.services;

import ru.otus.hw.domain.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> findAll();
}
