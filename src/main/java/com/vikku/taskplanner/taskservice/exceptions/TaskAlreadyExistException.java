package com.vikku.taskplanner.taskservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class TaskAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6022772833721511661L;

    private static final HttpStatus status = HttpStatus.CONFLICT;

    private static String DEFAULT_MESSAGE = """
            Task already exists in database
            """;

    public TaskAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskAlreadyExistException(String taskId) {
        super("Task with id " + taskId  + "already exists in database");
    }
}
