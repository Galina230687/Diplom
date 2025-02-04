package ru.netology.cloudStorage.exception;

public class UserNotFoundException extends RuntimeException {

    private final long id;

    public UserNotFoundException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}