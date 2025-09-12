package com.vikku.taskplanner.authservice.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String username;
    private String message;
    private String error;
}
