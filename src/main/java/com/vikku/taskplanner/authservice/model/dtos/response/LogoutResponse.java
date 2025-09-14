package com.vikku.taskplanner.authservice.model.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse {
    private String message;
}
