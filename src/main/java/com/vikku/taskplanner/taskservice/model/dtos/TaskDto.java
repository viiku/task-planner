package com.vikku.taskplanner.taskservice.model.dtos;

import com.vikku.taskplanner.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String name;
    private String problemId;
    private String description;
    private TaskType taskType;
    private TaskDifficulty taskDifficulty;
    private TaskStatus taskStaus;
    private String url;
}
