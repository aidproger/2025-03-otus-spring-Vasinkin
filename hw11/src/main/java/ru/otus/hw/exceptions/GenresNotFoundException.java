package ru.otus.hw.exceptions;

public class GenresNotFoundException extends RuntimeException {

    public GenresNotFoundException() {
        super("Genre not found");
    }

    public GenresNotFoundException(String message) {
        super(message);
    }

}
