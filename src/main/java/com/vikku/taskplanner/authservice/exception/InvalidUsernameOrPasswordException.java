package com.vikku.taskplanner.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidUsernameOrPasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6022211822892929219L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static final String DEFAULT_MESSAGE = """
            Invalid username or password
            """;

    public InvalidUsernameOrPasswordException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidUsernameOrPasswordException(final String username, final String email) {
        super("Invalid username " + username + " or " + email);
    }
}
