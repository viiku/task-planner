package com.vikku.taskservice.taskservice.model.entities;


import com.vikku.taskservice.common.model.entity.BaseEntity;
import com.vikku.taskservice.taskservice.model.enums.TaskDifficulty;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_task_type_status", columnList = "taskType, taskStatus"),
//                @Index(name = "idx_due_date", columnList = "dueDate")
        }
)
public class TaskEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "task_id", nullable = false, unique = true)
    private String taskId;

    @Column(name = "task_name", nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskDifficulty taskDifficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus taskStatus;

    @Column(unique = true)
    private String url;

    private Integer estimatedMinutes = 50;
    private Integer actualMinutes = 50;
    private String notes;
}
