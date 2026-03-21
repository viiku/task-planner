package com.vikku.taskservice.model.mappers;

import com.vikku.taskservice.common.model.mapper.BaseMapper;
import com.vikku.taskservice.model.dtos.TaskDto;
import com.vikku.taskservice.model.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements BaseMapper<TaskDto, TaskEntity> {

    @Override
    public TaskEntity toEntity(TaskDto dto) {
        return null;
    }

    @Override
    public TaskDto toDto(TaskEntity entity) {
        return TaskDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .taskType(entity.getTaskType())
                .taskDifficulty(entity.getTaskDifficulty())
                .taskStaus(entity.getTaskStatus())
                .url(entity.getUrl())
                .build();
    }
}
