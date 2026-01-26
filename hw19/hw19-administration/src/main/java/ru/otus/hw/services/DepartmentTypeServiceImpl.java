package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.DepartmentTypeDto;
import ru.otus.hw.exceptions.DepartmentTypeNotFoundException;
import ru.otus.hw.repositories.DepartmentTypeRepository;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class DepartmentTypeServiceImpl implements DepartmentTypeService {

    private final Logger log = LoggerFactory.getLogger(DepartmentTypeServiceImpl.class);

    private final DepartmentTypeRepository departmentTypeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<DepartmentTypeDto> findAll() {
        log.info("Find all data");
        var departmentTypes = departmentTypeRepository.findAll().stream()
                .map(dt -> new DepartmentTypeDto(dt.getId(), dt.getName()))
                .toList();
        if (isEmpty(departmentTypes)) {
            throw new DepartmentTypeNotFoundException();
        }
        return departmentTypes;
    }
}
