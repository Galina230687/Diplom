package ru.netology.cloudStorage.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.cloudStorage.exception.FileNotFoundException;
import ru.netology.cloudStorage.exception.InvalidInputDataException;
import ru.netology.cloudStorage.exception.UserAlreadyExistsException;
import ru.netology.cloudStorage.exception.UserNotFoundException;
import ru.netology.cloudStorage.model.ErrorMessage;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage fileNotFoundHandler(FileNotFoundException e) {
        return new ErrorMessage(e.getMessage(), e.getId());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundHandler(UserNotFoundException e) {
        return new ErrorMessage(e.getMessage(), e.getId());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage userAlreadyCreatedHandler(UserAlreadyExistsException e) {
        return new ErrorMessage(e.getMessage(), e.getId());
    }

    @ExceptionHandler(InvalidInputDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage uInvalidInputDataHandler(InvalidInputDataException e) {
        return new ErrorMessage(e.getMessage(), e.getId());
    }
}
