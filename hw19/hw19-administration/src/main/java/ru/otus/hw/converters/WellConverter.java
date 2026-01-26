package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.WellDto;
import ru.otus.hw.models.Well;

@RequiredArgsConstructor
@Component
public class WellConverter {

    private final DepartmentConverter departmentConverter;

    public WellDto convertEntityToDto(Well well) {
        return new WellDto(well.getId(), well.getSgtiCode(), well.getCodeName(), well.getFullName(),
                well.getCreationDate(), well.getCompletionDate(), well.getBeginAbsoluteMeter(),
                well.getEndAbsoluteMeter(), well.isArchive(),
                well.getDepartment() != null ? departmentConverter.convertEntityToDto(well.getDepartment()) : null);
    }
}
