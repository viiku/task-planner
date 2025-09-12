package com.vikku.taskplanner.authservice.controller;

import com.vikku.taskplanner.authservice.model.dtos.request.LoginRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.LogoutRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.RefreshTokenRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.SignupRequest;
import com.vikku.taskplanner.authservice.model.dtos.response.*;
import com.vikku.taskplanner.authservice.service.AuthService;
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

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signin(loginRequest));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @PostMapping("/signout")
    public ResponseEntity<SignoutResponse> logoutUser(@Valid @RequestBody LogoutRequest logoutRequest) {
        return null;
    }

//
//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest requestToken) {
//
//        RefreshTokenEntity refreshToken = refreshTokenRepository.findByRefreshToken(requestToken.getToken())
//                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
//
//        refreshTokenService.verifyExpiration(refreshToken);
//
//        String newToken = jwtService.generateToken(refreshToken.getUser());
//        return ResponseEntity.ok(Map.of("accessToken", newToken));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestBody TokenInvalidateRequest tokenInvalidateRequest) {
//
//        LogoutResponse response = logoutService.logout(tokenInvalidateRequest);
//        return ResponseEntity.ok(response);
//    }

}
