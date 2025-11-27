package com.vikku.taskservice.taskservice.service;

import com.vikku.taskservice.taskservice.model.dtos.TaskDto;
import com.vikku.taskservice.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskservice.taskservice.model.dtos.request.TaskUpdateRequest;
import com.vikku.taskservice.taskservice.model.dtos.response.TaskResponse;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskCreateRequest taskRequest);

    TaskResponse updateTask(String taskId, TaskUpdateRequest updateTaskRequest);

    TaskDto getTask(String taskId);

    void deleteTask(String taskId);

    List<TaskDto> getAllTasksByTypeAndStatus(TaskType type, TaskStatus status);

    List<TaskDto> getTaskByType(TaskType type);
}
