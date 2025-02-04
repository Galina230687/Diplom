package ru.netology.cloudStorage.exception;

public class InvalidInputDataException extends RuntimeException {

    private final long id;

    public InvalidInputDataException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}