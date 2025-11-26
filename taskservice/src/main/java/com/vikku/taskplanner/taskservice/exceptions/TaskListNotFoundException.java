package com.vikku.taskplanner.taskservice.exceptions;

import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskListNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -6022302209715111661L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public static final String DEFAULT_MESSAGE = """
            Task list not found in database by type and status
            """;

    public TaskListNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskListNotFoundException(final TaskType type, final TaskStatus status) {
        super("Tasks of type " + type + "and status " + status + " not found in database");
    }
}
