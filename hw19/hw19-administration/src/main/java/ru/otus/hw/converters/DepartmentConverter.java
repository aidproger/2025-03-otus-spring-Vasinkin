package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.DepartmentDto;
import ru.otus.hw.domain.DepartmentTypeDto;
import ru.otus.hw.models.Department;

@Component
public class DepartmentConverter {

    public DepartmentDto convertEntityToDto(Department department) {
        var departmentType = new DepartmentTypeDto(department.getDepartmentType().getId(),
                department.getDepartmentType().getName());
        return new DepartmentDto(department.getId(), department.getName(), department.isDefault(),
                departmentType, department.getParent() != null ? department.getParent().getId() : null);
    }
}
