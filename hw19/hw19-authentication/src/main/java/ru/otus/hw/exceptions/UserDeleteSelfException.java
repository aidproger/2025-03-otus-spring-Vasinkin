package ru.otus.hw.exceptions;

public class UserDeleteSelfException extends RuntimeException {

    public UserDeleteSelfException() {
        super("User can not delete self");
    }

    public UserDeleteSelfException(String message) {
        super(message);
    }
}
