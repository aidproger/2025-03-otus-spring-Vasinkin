package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.PasswordsNotEqualsException;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.exceptions.UserDeleteSelfException;
import ru.otus.hw.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handeException(Exception ex) {
        log.error("Unknown error:{}", ex.getMessage());
        String errorText = messageSource.getMessage("unknown-error-text", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorText);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(UserNotFoundException ex) {
        log.error("User not found:{}", ex.getMessage());
        String errorText = messageSource.getMessage("user-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorText);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(RoleNotFoundException ex) {
        log.error("Role not found:{}", ex.getMessage());
        String errorText = messageSource.getMessage("role-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorText);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("Fields validation:{}", ex.getMessage());
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(PasswordsNotEqualsException.class)
    public ResponseEntity<Map<String, List<String>>> handePasswordsNotEqualsException(PasswordsNotEqualsException ex) {
        log.error("Passwords not equals:{}", ex.getMessage());
        String errorText = messageSource.getMessage("passwords-not-equals-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("password", List.of(errorText), "passwordConfirm", List.of(errorText)));
    }

    @ExceptionHandler(UserDeleteSelfException.class)
    public ResponseEntity<Map<String, String>> handeCannotDeleteSelfException(UserDeleteSelfException ex) {
        log.error("User delete self:{}", ex.getMessage());
        String errorText = messageSource.getMessage("user-delete-self-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", errorText));
    }

}
