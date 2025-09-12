package com.vikku.taskplanner.authservice.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignupResponse {
    private String message;
}
