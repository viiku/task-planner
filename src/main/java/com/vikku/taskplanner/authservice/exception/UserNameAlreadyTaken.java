package com.vikku.taskplanner.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameAlreadyTaken extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -602220133721511661L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static String DEFAULT_MESSAGE = """
            Username already exists in database
            """;

    public UserNameAlreadyTaken() {
        super(DEFAULT_MESSAGE);
    }

    public UserNameAlreadyTaken(String username) {
        super("Username with name " + username  + "already exists in database");
    }
}
