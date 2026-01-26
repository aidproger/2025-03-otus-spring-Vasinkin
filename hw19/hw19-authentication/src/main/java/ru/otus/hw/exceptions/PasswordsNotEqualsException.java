package ru.otus.hw.exceptions;

public class PasswordsNotEqualsException extends RuntimeException {

    public PasswordsNotEqualsException() {
        super("Passwords not equals");
    }

    public PasswordsNotEqualsException(String message) {
        super(message);
    }

}
