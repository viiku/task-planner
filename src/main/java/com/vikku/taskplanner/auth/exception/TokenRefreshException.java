package com.vikku.taskplanner.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class TokenRefreshException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2982829291919111661L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static final String DEFAULT_MESSAGE = """
            Refresh token was expired or revoked. Please make a new signin request
            """;

    public TokenRefreshException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenRefreshException(final String refreshToken) {
        super("Refresh token " + refreshToken  + "is expired or revoked. Please make a new signin request");
    }

    public TokenRefreshException(final String refreshToken, final String message) {
        super("Refresh token " + refreshToken  + "Refresh token is not in database!");
    }
}
