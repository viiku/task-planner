package com.vikku.taskplanner.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenInvalidException extends RuntimeException {


}
