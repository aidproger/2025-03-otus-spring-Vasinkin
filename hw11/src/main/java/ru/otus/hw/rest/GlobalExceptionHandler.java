package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.AuthorNotFoundException;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthorNotFoundException.class)
    public Mono<ProblemDetail> handeNotFoundException(AuthorNotFoundException ex,
                                                      ServerWebExchange serverWebExchange) {
        log.error("Author not found", ex);

        String errorText = messageSource.getMessage("author-not-found-error", null,
                LocaleContextHolder.getLocale());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, errorText);
        problemDetail.setTitle("Author not found");
        problemDetail.setInstance(URI.create(serverWebExchange.getRequest().getURI().getPath()));

        return Mono.just(problemDetail);
    }

}
