package ru.otus.hw.domain;

import java.util.Set;

public record UserResponseDto(long id,
                              String login,
                              String password,
                              String passwordConfirm,
                              String lastName,
                              String firstName,
                              String middleName,
                              boolean active,
                              Set<Long> rolesIds) {
}
