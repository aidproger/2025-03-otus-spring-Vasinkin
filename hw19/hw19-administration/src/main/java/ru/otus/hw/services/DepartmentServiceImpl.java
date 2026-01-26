package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.DepartmentConverter;
import ru.otus.hw.domain.DepartmentDto;
import ru.otus.hw.exceptions.DepartmentNotFoundException;
import ru.otus.hw.repositories.DepartmentRepository;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentConverter departmentConverter;

    @Transactional(readOnly = true)
    @Override
    public List<DepartmentDto> findAll() {
        log.info("Find all data");
        var departments = departmentRepository.findAll().stream()
                .map(departmentConverter::convertEntityToDto)
                .toList();
        if (isEmpty(departments)) {
            throw new DepartmentNotFoundException();
        }
        return departments;
    }
}
