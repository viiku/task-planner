package com.vikku.taskplanner.taskservice.exceptions;

import com.vikku.taskplanner.taskservice.model.enums.TaskType;

import java.io.Serial;

public class TaskNotFoundException extends RuntimeException {

    @Serial
    public static Long serialVersionId = -12328292919L;

    public static String DEFAULT_MESSAGE = "Task not found in database";

    public TaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskNotFoundException(Long taskId) {
        super(taskId + " " + DEFAULT_MESSAGE);
    }
}
