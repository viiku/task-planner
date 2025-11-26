package com.vikku.taskplanner.taskservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6022779915715111661L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public static final String DEFAULT_MESSAGE = "Task not found in database";

    public TaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskNotFoundException(final String taskId) {
        super("Task with id " + taskId + " not found in database");
    }
}
