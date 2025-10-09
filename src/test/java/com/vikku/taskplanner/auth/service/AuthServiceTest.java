package com.vikku.taskplanner.service;

import com.vikku.taskplanner.auth.model.dtos.request.SignupRequest;
import com.vikku.taskplanner.auth.model.dtos.response.SignupResponse;
import com.vikku.taskplanner.auth.model.entity.UserEntity;
import com.vikku.taskplanner.auth.repository.UserRepository;
import com.vikku.taskplanner.auth.service.AuthService;
import com.vikku.taskplanner.auth.service.JwtService;
import com.vikku.taskplanner.auth.service.RefreshTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegister_success() {
        SignupRequest request = SignupRequest.builder()
                .username("test")
                .email("test@example.com")
                .password("test")
                .firstName("test")
                .lastName("user")
                .phoneNumber("90892387")
                .build();

        Mockito.when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenAnswer(i -> i.getArgument(0));

        SignupResponse response = authService.register(request);

        Assertions.assertEquals("User registered successfully!", response.getMessage());
        Mockito.verify(userRepository).save(Mockito.any(UserEntity.class));
    }
}
