package ru.otus.hw.services;

import ru.otus.hw.domain.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> findAll();

}
