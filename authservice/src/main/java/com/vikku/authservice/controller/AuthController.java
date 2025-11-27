package com.vikku.authservice.controller;

import com.vikku.authservice.model.dtos.request.LoginRequest;
import com.vikku.authservice.model.dtos.request.SignupRequest;
import com.vikku.authservice.model.dtos.request.TokenRefreshRequest;
import com.vikku.authservice.model.dtos.response.LoginResponse;
import com.vikku.authservice.model.dtos.response.SignupResponse;
import com.vikku.authservice.service.AuthService;
import com.vikku.authservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<LoginResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(value = "/refreshtoken", produces = "application/json")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(tokenRefreshRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        return ResponseEntity.ok(refreshTokenService.logout(request));
    }

    @PostMapping("/logout-all-devices")
    public ResponseEntity<?> logoutAllDevices(HttpServletRequest request) {
        return ResponseEntity.ok(refreshTokenService.logoutFromAllServices(request));
    }
}
