package ru.otus.hw.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserDto(long id,
                            @NotBlank @Size(min = 5, max = 100)
                            String login,
                            @Size(min = 8, max = 100)
                            String password,
                            @Size(min = 8, max = 100)
                            String passwordConfirm,
                            @NotBlank @Size(min = 3, max = 255)
                            String lastName,
                            @NotBlank @Size(min = 3, max = 255)
                            String firstName,
                            @NotNull @Size(max = 255)
                            String middleName,
                            @NotNull
                            boolean active,
                            List<RoleDto> roles) {
}
