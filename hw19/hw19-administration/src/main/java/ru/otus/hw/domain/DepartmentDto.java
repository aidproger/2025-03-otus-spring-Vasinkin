package ru.otus.hw.domain;

public record DepartmentDto(long id, String name, boolean isDefault,
                            DepartmentTypeDto departmentType, Long parentId) {
}
