package com.vikku.taskplanner.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailAlreadyExistException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -6022212923922911661L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static final String DEFAULT_MESSAGE = """
            User email already exists in database
            """;

    public UserEmailAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public UserEmailAlreadyExistException(final String email) {
        super("User with email " + email  + "already exists in database");
    }
}
