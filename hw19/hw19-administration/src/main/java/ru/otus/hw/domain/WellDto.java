package ru.otus.hw.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.otus.hw.common.LocaleLocalDateTimeSerializer;

import java.time.LocalDateTime;

public record WellDto(long id,
                      String sgtiCode,
                      String codeName,
                      String fullName,
                      @JsonSerialize(using = LocaleLocalDateTimeSerializer.class)
                      LocalDateTime creationDate,
                      @JsonSerialize(using = LocaleLocalDateTimeSerializer.class)
                      LocalDateTime completionDate,
                      Float beginAbsoluteMeter,
                      Float endAbsoluteMeter,
                      boolean archive,
                      DepartmentDto department) {
}
