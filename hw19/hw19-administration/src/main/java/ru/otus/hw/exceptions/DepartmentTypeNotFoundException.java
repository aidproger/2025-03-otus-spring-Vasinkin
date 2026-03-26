package ru.otus.hw.exceptions;

public class DepartmentTypeNotFoundException extends RuntimeException {

    public DepartmentTypeNotFoundException() {
        super("Department type not found");
    }

    public DepartmentTypeNotFoundException(String message) {
        super(message);
    }

}
