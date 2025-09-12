package com.vikku.taskplanner.authservice.service;

import com.vikku.taskplanner.authservice.model.dtos.request.LogoutRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.RefreshTokenRequest;
import com.vikku.taskplanner.authservice.model.dtos.response.LogoutResponse;
import com.vikku.taskplanner.authservice.model.dtos.response.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String jwtToken = refreshTokenRequest.getRefreshToken();
        if (jwtService.(jwtToken)) {
            return RefreshTokenResponse.builder()
                    .accessToken(null)
                    .refreshToken(null)
                    .message("Access token is not expired")
                    .build();
        }

        jwtService.generateJwtToken()
        return null;
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        return null;
    }
}
