package ru.otus.hw.exceptions;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException() {
        super("Department not found");
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
