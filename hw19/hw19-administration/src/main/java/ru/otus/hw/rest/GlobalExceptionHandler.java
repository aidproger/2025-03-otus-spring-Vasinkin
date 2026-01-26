package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.DepartmentNotFoundException;
import ru.otus.hw.exceptions.DepartmentTypeNotFoundException;
import ru.otus.hw.exceptions.WellNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handeException(Exception ex) {
//        log.error("Unknown error:{}", ex.getMessage());
//        String errorText = messageSource.getMessage("unknown-error-text", null,
//                LocaleContextHolder.getLocale());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorText);
//    }

    @ExceptionHandler(DepartmentTypeNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(DepartmentTypeNotFoundException ex) {
        log.error("Department type not found:{}", ex.getMessage());
        String errorText = messageSource.getMessage("department-type-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorText);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(DepartmentNotFoundException ex) {
        log.error("Department not found:{}", ex.getMessage());
        String errorText = messageSource.getMessage("department-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorText);
    }

    @ExceptionHandler(WellNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(WellNotFoundException ex) {
        log.error("Well not found:{}", ex.getMessage());
        String errorText = messageSource.getMessage("well-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorText);
    }

}
