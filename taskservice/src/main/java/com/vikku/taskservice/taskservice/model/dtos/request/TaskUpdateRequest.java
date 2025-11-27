package com.vikku.taskservice.taskservice.model.dtos.request;

import com.vikku.taskservice.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {

    private String name;
    private String problemId;
    private String description;
    private TaskType taskType;
    private TaskDifficulty taskDifficulty;
    private TaskStatus taskStatus;
    private String url;
    private String notes;
}
