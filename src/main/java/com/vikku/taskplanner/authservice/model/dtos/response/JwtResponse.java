package com.vikku.taskplanner.authservice.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type;
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private List<String> roles;
}
