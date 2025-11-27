package com.vikku.taskservice.taskservice.model.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String taskId;
    private String message;
    private String error;
}
