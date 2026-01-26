package ru.otus.hw.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.otus.hw.common.LocaleLocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(long id,
                      @NotBlank @Size(min = 5, max = 100)
                      String login,
                      @NotBlank @Size(min = 8, max = 100)
                      String password,
                      @NotBlank @Size(min = 8, max = 100)
                      String passwordConfirm,
                      @NotBlank @Size(min = 3, max = 255)
                      String lastName,
                      @NotBlank @Size(min = 3, max = 255)
                      String firstName,
                      @NotNull @Size(max = 255)
                      String middleName,
                      @JsonSerialize(using = LocaleLocalDateTimeSerializer.class)
                      LocalDateTime registrationDate,
                      @JsonSerialize(using = LocaleLocalDateTimeSerializer.class)
                      LocalDateTime lastAccessedAt,
                      @NotNull
                      boolean active,
                      List<RoleDto> roles) {

}
