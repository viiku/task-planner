package com.vikku.taskplanner.authservice.controller;

import com.vikku.taskplanner.authservice.model.dtos.request.LoginRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.SignupRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.TokenRefreshRequest;
import com.vikku.taskplanner.authservice.model.dtos.response.*;
import com.vikku.taskplanner.authservice.service.AuthService;
import com.vikku.taskplanner.authservice.service.JwtService;
import com.vikku.taskplanner.authservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signin(loginRequest));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(tokenRefreshRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        return ResponseEntity.ok(refreshTokenService.logout(request));
    }

    @PostMapping("/logout-all-devices")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutAllDevices(HttpServletRequest request) {
        return ResponseEntity.ok(refreshTokenService.logoutFromAllServices(request));
    }
}
