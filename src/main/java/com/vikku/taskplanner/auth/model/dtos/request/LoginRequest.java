package com.vikku.taskplanner.auth.model.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
//    @Size(min = 6, max = 40)
    private String password;
}
