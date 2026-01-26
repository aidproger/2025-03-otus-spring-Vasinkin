package ru.otus.hw.exceptions;

public class WellNotFoundException extends RuntimeException {

    public WellNotFoundException() {
        super("Well not found");
    }

    public WellNotFoundException(String message) {
        super(message);
    }
}
