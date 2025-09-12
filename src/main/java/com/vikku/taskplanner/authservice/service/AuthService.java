package com.vikku.taskplanner.authservice.service;

import com.vikku.taskplanner.authservice.exception.UserEmailAlreadyExistException;
import com.vikku.taskplanner.authservice.exception.UserNameAlreadyTaken;
import com.vikku.taskplanner.authservice.model.dtos.request.LoginRequest;
import com.vikku.taskplanner.authservice.model.dtos.request.SignupRequest;
import com.vikku.taskplanner.authservice.model.dtos.response.*;
import com.vikku.taskplanner.authservice.model.entity.UserEntity;
import com.vikku.taskplanner.authservice.model.enums.UserRole;
import com.vikku.taskplanner.authservice.model.enums.UserStatus;
import com.vikku.taskplanner.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public SignupResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserNameAlreadyTaken(signupRequest.getUsername());
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserEmailAlreadyExistException(signupRequest.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .roles(Collections.singleton(UserRole.USER))
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
        return SignupResponse.builder()
                .message("User registered successfully!")
                .build();
    }

    public SigninResponse signin(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateJwtToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // Update last login and reset failed attempts
            updateUserLoginSuccess(userDetails.getId());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return SigninResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshToken)
                    .type("Bearer")
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .fullName(userDetails.getFullName())
                    .roles(roles)
                    .message("User Logged in successfully")
                    .build();

        } catch (BadCredentialsException e) {
            handleFailedLogin(loginRequest.getUsername());
            return SigninResponse.builder()
                    .message("Invalid username or password")
                    .build();
        }
    }

    @Transactional
    private void updateUserLoginSuccess(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
            userRepository.save(user);
        }
    }

    @Transactional
    private void handleFailedLogin(String username) {
        userRepository.findActiveByUsernameOrEmail(username).ifPresent(user -> {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= 5) {
                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(30));
                logger.warn("Account locked for user: {} due to {} failed login attempts",
                        username, attempts);
            }
            userRepository.save(user);
        });
    }
}
