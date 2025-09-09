package com.vikku.taskplanner.taskservice.model.entities;


import com.vikku.taskplanner.common.model.entity.BaseEntity;
import com.vikku.taskplanner.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String problemId;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private TaskDifficulty taskDifficulty;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStaus;

    private String url;
    private Integer estimatedMinutes = 50;
    private Integer actualMinutes = 50;
    private String notes;
}
