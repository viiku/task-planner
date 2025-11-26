package com.vikku.taskplanner.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6029283828282511661L;

    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = """
            Refresh token not found
            """;

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String username) {
        super("User " + username  + "not found in database");
    }
}
