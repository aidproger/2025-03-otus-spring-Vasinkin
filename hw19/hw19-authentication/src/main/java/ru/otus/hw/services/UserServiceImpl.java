package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.domain.UserDto;
import ru.otus.hw.domain.UserResponseDto;
import ru.otus.hw.exceptions.PasswordsNotEqualsException;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.exceptions.UserDeleteSelfException;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        log.info("Find all data");
        var users = userRepository.findAll().stream()
                .map(userConverter::convertEntityToDto)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(long id) {
        log.info("Find data by id:{}", id);
        return userRepository.findById(id)
                .map(userConverter::convertEntityToDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public UserDto insert(UserResponseDto userResponseDto) {
        log.info("Insert data login:{}, lastName:{}, firstName:{}, middleName:{}, active{}, roles ids:{}",
                userResponseDto.login(), userResponseDto.lastName(), userResponseDto.firstName(),
                userResponseDto.middleName(), userResponseDto.active(), userResponseDto.rolesIds());
        if (!userResponseDto.password().equals(userResponseDto.passwordConfirm())) {
            throw new PasswordsNotEqualsException();
        }
        var user = save(userResponseDto);

        return userConverter.convertEntityToDto(user);
    }

    @Transactional
    @Override
    public UserDto update(UserResponseDto userResponseDto) {
        log.info("Update data by id:{}, login:{}, lastName:{}, firstName:{}, middleName:{}, active{}, roles ids:{}",
                userResponseDto.id(), userResponseDto.login(), userResponseDto.lastName(), userResponseDto.firstName(),
                userResponseDto.middleName(), userResponseDto.active(), userResponseDto.rolesIds());
        if (!Objects.equals(userResponseDto.password(), userResponseDto.passwordConfirm())) {
            throw new PasswordsNotEqualsException();
        }
        var user = save(userResponseDto);
        return userConverter.convertEntityToDto(user);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        log.info("Delete data by id:{}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id %d not found".formatted(id)));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userLoginAuthentication = ((org.springframework.security.core.userdetails.User)
                securityContext.getAuthentication().getPrincipal()).getUsername();
        if (Objects.equals(user.getLogin(), userLoginAuthentication)) {
            throw new UserDeleteSelfException();
        }

        userRepository.delete(user);
    }

    private User save(UserResponseDto userDto) {
        List<Role> roles;
        User user;
        if (isEmpty(userDto.rolesIds())) {
            roles = List.of();
        } else {
            roles = roleRepository.findAllById(userDto.rolesIds());
            if (isEmpty(roles) || userDto.rolesIds().size() != roles.size()) {
                throw new RoleNotFoundException("One or all roles with ids %s not found".formatted(userDto.rolesIds()));
            }
        }
        if (userDto.id() == 0) {
            user = new User(0, userDto.login(), passwordEncoder.encode(userDto.password()),
                    userDto.lastName(), userDto.firstName(), userDto.middleName(),
                    LocalDateTime.now(), null, userDto.active(), roles);
        } else {
            var userOld = userRepository.findById(userDto.id()).orElseThrow(
                    () -> new UserNotFoundException("User with id %d not found".formatted(userDto.id())));
            user = new User(userDto.id(), userDto.login(),
                    userDto.password() != null ? passwordEncoder.encode(userDto.password()) : userOld.getPassword(),
                    userDto.lastName(), userDto.firstName(), userDto.middleName(), userOld.getRegistrationDate(),
                    userOld.getLastAccessedAt(), userDto.active(), roles);
        }
        return userRepository.save(user);
    }
}
