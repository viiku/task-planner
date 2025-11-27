package com.vikku.taskservice.taskservice.service.impl;

import com.vikku.taskservice.taskservice.exceptions.TaskAlreadyExistException;
import com.vikku.taskservice.taskservice.exceptions.TaskListNotFoundException;
import com.vikku.taskservice.taskservice.exceptions.TaskNotFoundException;
import com.vikku.taskservice.taskservice.model.dtos.TaskDto;
import com.vikku.taskservice.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskservice.taskservice.model.dtos.request.TaskUpdateRequest;
import com.vikku.taskservice.taskservice.model.dtos.response.TaskResponse;
import com.vikku.taskservice.taskservice.model.entities.TaskEntity;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;
import com.vikku.taskservice.taskservice.model.mappers.TaskMapper;
import com.vikku.taskservice.taskservice.repository.TaskRepository;
import com.vikku.taskservice.taskservice.service.TaskService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final Counter taskCreatedCounter;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, MeterRegistry registry) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskCreatedCounter = Counter.builder("task.created.count")
                .description("Number of tasks created")
                .register(registry);
    }

    @Transactional
    @Override
    public TaskResponse createTask(TaskCreateRequest taskRequest) {

        if(taskRepository.existsByTaskId(taskRequest.getTaskId())) {
            throw new TaskAlreadyExistException("Task already exists with this task id");
        }

        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(taskRequest.getTaskId())
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .taskType(taskRequest.getTaskType())
                .taskDifficulty(taskRequest.getTaskDifficulty())
                .taskStatus(taskRequest.getTaskStatus())
                .url(taskRequest.getUrl())
                .estimatedMinutes(50)
                .actualMinutes(50)
                .notes(taskRequest.getNotes())
                .build();

        taskRepository.save(taskEntity);
        taskCreatedCounter.increment();
        return TaskResponse.builder()
                .taskId(taskRequest.getTaskId())
                .message("Task created successfully with " + taskRequest.getTaskId())
                .error(null)
                .build();
    }

    @Transactional
    @Override
    public TaskResponse updateTask(String taskId, TaskUpdateRequest request) {
        TaskEntity taskEntity = taskRepository.findByTaskId(taskId);
        if(taskEntity == null) {
            throw  new TaskNotFoundException(taskId);
        }

        taskEntity.setName(request.getName());
//        taskEntity.setTaskId(request.getProblemId());
        taskEntity.setDescription(request.getDescription());
        taskEntity.setTaskType(request.getTaskType());
        taskEntity.setTaskDifficulty(request.getTaskDifficulty());
        taskEntity.setTaskStatus(request.getTaskStatus());
        taskEntity.setUrl(request.getUrl());
        taskEntity.setEstimatedMinutes(45);
        taskEntity.setActualMinutes(60);
        taskEntity.setNotes(request.getNotes());

        taskRepository.save(taskEntity);
        return TaskResponse.builder()
                .taskId(taskEntity.getTaskId())
                .message("Task updated successfully")
                .error(null)
                .build();
    }

    @Override
    public TaskDto getTask(String taskId) {
        TaskEntity taskEntity = taskRepository.findByTaskId(taskId);
        if(taskEntity == null) {
            throw new TaskNotFoundException(taskId);
        }
        return taskMapper.toDto(taskEntity);
    }

    @Transactional
    @Override
    public void deleteTask(String taskId) {
        if(!taskRepository.existsByTaskId(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteByTaskId(taskId);
    }

    @Override
    public List<TaskDto> getAllTasksByTypeAndStatus(TaskType type, TaskStatus status) {
        List<TaskEntity> entities = taskRepository.findAllByTaskTypeAndTaskStatus(type, status);
        if(entities.isEmpty()) {
            throw new TaskListNotFoundException(type, status);
        }
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(TaskEntity entity: entities) {
            taskDtoList.add(taskMapper.toDto(entity));
        }
        return taskDtoList;
    }

    @Override
    public List<TaskDto> getTaskByType(TaskType type) {
        List<TaskEntity> entities = taskRepository.findAllTaskByTaskType(type);
        if(entities.isEmpty()) {
            throw new TaskNotFoundException();
        }
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(TaskEntity entity: entities) {
            taskDtoList.add(taskMapper.toDto(entity));
        }
        return taskDtoList;
    }
}
