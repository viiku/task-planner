package com.vikku.taskservice.taskservice.model.dtos;

import com.vikku.taskservice.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private TaskType taskType;
    private TaskDifficulty taskDifficulty;
    private TaskStatus taskStaus;
    private String url;
}
