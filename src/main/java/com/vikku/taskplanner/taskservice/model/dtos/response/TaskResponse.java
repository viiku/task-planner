package com.vikku.taskplanner.taskservice.model.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long taskId;
    private String message;
    private String error;
}
