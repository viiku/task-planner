package com.vikku.taskplanner.taskservice.service;

import com.vikku.taskplanner.taskservice.model.dtos.TaskDto;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskCreateRequest taskRequest);

    TaskDto updateTask(TaskCreateRequest updateTaskRequest);

    TaskDto getTask(Long taskId);

    void deleteTask(Long taskId);

    List<TaskDto> getTasks(TaskType type, TaskStatus status);

    List<TaskDto> getTaskByType(TaskType type);
}
