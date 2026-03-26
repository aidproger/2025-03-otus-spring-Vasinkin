package ru.otus.hw.services;

import ru.otus.hw.domain.DepartmentTypeDto;

import java.util.List;

public interface DepartmentTypeService {

    List<DepartmentTypeDto> findAll();
}
