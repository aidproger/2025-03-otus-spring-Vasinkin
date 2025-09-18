package ru.otus.hw.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Book not found");
    }

    public BookNotFoundException(String message) {
        super(message);
    }

}
