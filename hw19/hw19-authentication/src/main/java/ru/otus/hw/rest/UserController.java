package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.domain.RoleDto;
import ru.otus.hw.domain.UpdateUserDto;
import ru.otus.hw.domain.UserDto;
import ru.otus.hw.domain.UserResponseDto;
import ru.otus.hw.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping("/api/v1/users")
    public List<UserDto> getAllUsers() {
        log.info("Request all data");
        return userService.findAll();
    }

    @GetMapping("/api/v1/users/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        log.info("Request data with path variable id:{}", id);
        return userService.findById(id);
    }

    @DeleteMapping("/api/v1/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) {
        log.info("Request delete with path variable id:{}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Request add with request body userDto:{}", userDto);
        var rolesIds = userDto.roles().stream()
                .map(RoleDto::id).collect(Collectors.toSet());
        var userResponseDto = new UserResponseDto(0, userDto.login(), userDto.password(),
                userDto.passwordConfirm(), userDto.lastName(), userDto.firstName(), userDto.middleName(),
                userDto.active(), rolesIds);
        UserDto insertedUser = userService.insert(userResponseDto);
        return ResponseEntity.ok(insertedUser);
    }

    @PutMapping("/api/v1/users")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto userDto) {
        log.info("Request update with request body userDto:{}", userDto);
        var rolesIds = userDto.roles().stream()
                .map(RoleDto::id).collect(Collectors.toSet());
        var userResponseDto = new UserResponseDto(userDto.id(), userDto.login(), userDto.password(),
                userDto.passwordConfirm(), userDto.lastName(), userDto.firstName(), userDto.middleName(),
                userDto.active(), rolesIds);
        UserDto updatedUser = userService.update(userResponseDto);
        return ResponseEntity.ok(updatedUser);
    }

}
