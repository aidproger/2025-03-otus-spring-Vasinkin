package ru.otus.hw.services;

import ru.otus.hw.domain.WellDto;

import java.util.List;

public interface WellService {

    List<WellDto> findAll();
}
