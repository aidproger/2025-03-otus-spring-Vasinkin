package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.RoleDto;
import ru.otus.hw.domain.UserDto;
import ru.otus.hw.models.User;

@RequiredArgsConstructor
@Component
public class UserConverter {

    public UserDto convertEntityToDto(User user) {
        var roles = user.getRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .toList();
        return new UserDto(user.getId(), user.getLogin(), null, null,
                user.getLastName(), user.getFirstName(), user.getMiddleName(), user.getRegistrationDate(),
                user.getLastAccessedAt(), user.isActive(), roles);
    }
}
