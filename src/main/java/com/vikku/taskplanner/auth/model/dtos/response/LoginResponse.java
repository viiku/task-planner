package com.vikku.taskplanner.auth.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String refreshToken;
}
