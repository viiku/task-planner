package com.vikku.taskplanner.taskservice.model.dtos.request;

import com.vikku.taskplanner.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    private String taskId;
    private String name;
    private String description;
    private TaskType taskType;
    private TaskDifficulty taskDifficulty;
    private TaskStatus taskStatus;
    private String url;
    private String notes;
}
