package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.BookNotFoundException;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.CommentNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView handeNotFoundException(BookNotFoundException ex) {
        String errorText = messageSource.getMessage("book-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ModelAndView handeNotFoundException(CommentNotFoundException ex) {
        String errorText = messageSource.getMessage("comment-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }
}
