package com.vikku.taskplanner.taskservice.service.impl;

import com.vikku.taskplanner.taskservice.exceptions.TaskNotFoundException;
import com.vikku.taskplanner.taskservice.model.dtos.TaskDto;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskUpdateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.response.TaskResponse;
import com.vikku.taskplanner.taskservice.model.entities.TaskEntity;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import com.vikku.taskplanner.taskservice.model.mappers.TaskMapper;
import com.vikku.taskplanner.taskservice.repository.TaskRepository;
import com.vikku.taskplanner.taskservice.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .taskStatus(taskRequest.getTaskStatus())
                .url(taskRequest.getUrl())
                .estimatedMinutes(50)
                .actualMinutes(50)
                .notes(taskRequest.getNotes())
                .build();

        taskRepository.save(taskEntity);
        return taskMapper.toDto(taskEntity);
    }

    @Transactional
    @Override
    public TaskResponse updateTask(TaskUpdateRequest request) {

        TaskEntity taskEntity = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(request.getTaskId()));

        // update fields selectively
        taskEntity.setName(request.getName());
        taskEntity.setProblemId(request.getProblemId());
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
                .taskId(taskEntity.getId())
                .message("Task updated successfully")
                .error(null)
                .build();
    }

    @Override
    public TaskDto getTask(Long taskId) {

        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        return taskMapper.toDto(taskEntity);
    }

    @Override
    public void deleteTask(Long taskId) {

        if(!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskDto> getAllTasksByTypeAndStatus(TaskType type, TaskStatus status) {

        List<TaskEntity> entities = taskRepository.findAllByTaskStatusAndTaskType(type, status);

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
