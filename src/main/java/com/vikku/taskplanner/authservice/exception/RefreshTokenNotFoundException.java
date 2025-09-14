package com.vikku.taskplanner.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RefreshTokenNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -602243829221511661L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static final String DEFAULT_MESSAGE = """
            Refresh token not found
            """;

    public RefreshTokenNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public RefreshTokenNotFoundException(String refreshToken) {
        super("Refresh token " + refreshToken  + "not found in database");
    }
}
