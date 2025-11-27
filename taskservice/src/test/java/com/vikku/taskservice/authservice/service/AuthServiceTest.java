//package com.vikku.taskplanner.auth.service;
//
//import com.vikku.taskplanner.auth.exception.UserEmailAlreadyExistException;
//import com.vikku.taskplanner.auth.model.dtos.request.LoginRequest;
//import com.vikku.taskplanner.auth.model.dtos.request.SignupRequest;
//import com.vikku.taskplanner.auth.model.dtos.response.LoginResponse;
//import com.vikku.taskplanner.auth.model.dtos.response.SignupResponse;
//import com.vikku.taskplanner.auth.model.entity.UserEntity;
//import com.vikku.taskplanner.auth.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private AuthService authService;
//
//    // ---------- SIGNUP TESTS ----------
//    @Test
//    void testRegisterUser_Success() {
//        // Arrange
//        SignupRequest request = SignupRequest.builder()
//                .username("test")
//                .email("test@example.com")
//                .password("test")
//                .firstName("test")
//                .lastName("user")
//                .phoneNumber("90892387")
//                .build();
//
//        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
//        when(passwordEncoder.encode("test")).thenReturn("encodedPassword");
//        when(userRepository.save(any(UserEntity.class)))
//                .thenAnswer(i -> i.getArgument(0));
//
//        // Act
//        SignupResponse response = authService.register(request);
//
//        // Assert
//        Assertions.assertEquals("User registered successfully!", response.getMessage());
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    void testRegisterUser_EmailAlreadyExists() {
//        // Arrange
//        SignupRequest request = SignupRequest.builder()
//                .email("existing@example.com")
//                .build();
//
//        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
//
//        // Act & Assert
//        Assertions.assertThrows(UserEmailAlreadyExistException.class,
//                () -> authService.register(request));
//
//        verify(userRepository, never()).save(any());
//    }
//
//    // ---------- LOGIN TESTS ----------
//    @Test
//    void testLogin_Success() {
//        // Arrange
//        LoginRequest request = new LoginRequest();
//        request.setUsername("test");
//        request.setPassword("test");
//
//        Authentication authMock = mock(Authentication.class);
//        CustomUserDetails userDetails = mock(CustomUserDetails.class);
//
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authMock);
//        when(authMock.getPrincipal()).thenReturn(userDetails);
//        when(jwtService.generateJwtToken(userDetails)).thenReturn("jwtToken");
//        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refreshToken");
//        when(userDetails.getId()).thenReturn(1L);
//        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
//
//        doNothing().when(refreshTokenService).createRefreshToken(anyLong(), anyString());
//        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(new UserEntity()));
//
//        // Act
//        LoginResponse response = authService.login(request);
//
//        // Assert
//        Assertions.assertEquals("User Logged in successfully", response.getMessage());
//        Assertions.assertEquals("Bearer", response.getTokenType());
//        Assertions.assertEquals("jwtToken", response.getAccessToken());
//        verify(refreshTokenService, times(1)).createRefreshToken(anyLong(), anyString());
//    }
//
//    @Test
//    void testLogin_InvalidCredentials() {
//        // Arrange
//        LoginRequest request = new LoginRequest();
//        request.setUsername("invalid");
//        request.setPassword("wrong");
//
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new BadCredentialsException("Bad credentials"));
//
//        // Act
//        LoginResponse response = authService.login(request);
//
//        // Assert
//        Assertions.assertEquals("Invalid username or password", response.getMessage());
//        verify(userRepository, atLeast(0)).save(any());
//    }
//
//    // ---------- LOGOUT TESTS ----------
//    @Test
//    void testLogout_Success() {
//        // Suppose AuthService has logout(String token)
//        // We mock RefreshTokenService to invalidate it
//        doNothing().when(refreshTokenService).invalidateToken("refresh123");
//
//        authService.logout("refresh123");
//
//        verify(refreshTokenService, times(1)).invalidateToken("refresh123");
//    }
//
//    // ---------- LOGOUT ALL USERS TESTS ----------
//    @Test
//    void testLogoutAllUsers_Success() {
//        // Suppose AuthService has logoutAllUsers(Long userId)
//        doNothing().when(refreshTokenService).invalidateAllTokensForUser(1L);
//
//        authService.logoutAllUsers(1L);
//
//        verify(refreshTokenService, times(1)).invalidateAllTokensForUser(1L);
//    }
//}
