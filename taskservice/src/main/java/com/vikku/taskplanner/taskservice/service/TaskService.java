package com.vikku.taskplanner.taskservice.service;

import com.vikku.taskplanner.taskservice.model.dtos.TaskDto;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskUpdateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.response.TaskResponse;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskCreateRequest taskRequest);

    TaskResponse updateTask(String taskId, TaskUpdateRequest updateTaskRequest);

    TaskDto getTask(String taskId);

    void deleteTask(String taskId);

    List<TaskDto> getAllTasksByTypeAndStatus(TaskType type, TaskStatus status);

    List<TaskDto> getTaskByType(TaskType type);
}
