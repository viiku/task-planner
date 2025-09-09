package com.vikku.taskplanner.taskservice.service.impl;

import com.vikku.taskplanner.taskservice.model.dtos.TaskDto;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskplanner.taskservice.model.entities.TaskEntity;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import com.vikku.taskplanner.taskservice.model.mappers.TaskMapper;
import com.vikku.taskplanner.taskservice.repository.TaskRepository;
import com.vikku.taskplanner.taskservice.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    @Override
    public TaskDto createTask(TaskCreateRequest taskRequest) {

        TaskEntity taskEntity = TaskEntity.builder()
                .name(taskRequest.getName())
                .problemId(taskRequest.getProblemId())
                .description(taskRequest.getDescription())
                .taskType(taskRequest.getTaskType())
                .taskDifficulty(taskRequest.getTaskDifficulty())
                .taskStaus(taskRequest.getTaskStaus())
                .url(taskRequest.getUrl())
                .estimatedMinutes(50)
                .actualMinutes(50)
                .notes(taskRequest.getNotes())
                .build();

        taskRepository.save(taskEntity);
        return taskMapper.toDto(taskEntity);
    }

    @Override
    public TaskDto updateTask(TaskCreateRequest updateTaskRequest) {
        return null;
    }

    @Override
    public TaskDto getTask(Long taskId) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }

    @Override
    public List<TaskDto> getTasks(TaskType type, TaskStatus status) {
        return List.of();
    }

    @Override
    public List<TaskDto> getTaskByType(TaskType type) {
        return List.of();
    }
}
