package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.rest.exceptions.AuthorNotFoundException;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.rest.exceptions.CommentNotFoundException;
import ru.otus.hw.rest.exceptions.GenreNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(BookNotFoundException ex) {
        String errorText = messageSource.getMessage("book-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(404).body(errorText);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(CommentNotFoundException ex) {
        String errorText = messageSource.getMessage("comment-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(404).body(errorText);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(AuthorNotFoundException ex) {
        String errorText = messageSource.getMessage("author-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(404).body(errorText);
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(GenreNotFoundException ex) {
        String errorText = messageSource.getMessage("genre-not-found-error", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(404).body(errorText);
    }

}
