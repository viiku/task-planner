package com.vikku.taskplanner.authservice.service;

import com.vikku.taskplanner.authservice.exception.RefreshTokenNotFoundException;
import com.vikku.taskplanner.authservice.exception.TokenRefreshException;
import com.vikku.taskplanner.authservice.model.dtos.request.LogoutRequest;
import com.vikku.taskplanner.authservice.model.dtos.response.LogoutResponse;
import com.vikku.taskplanner.authservice.model.dtos.response.RefreshTokenResponse;
import com.vikku.taskplanner.authservice.model.entity.RefreshTokenEntity;
import com.vikku.taskplanner.authservice.model.entity.UserEntity;
import com.vikku.taskplanner.authservice.repository.RefreshTokenRepository;
import com.vikku.taskplanner.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${spring.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponse refreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken));

        UserEntity user = refreshTokenEntity.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateJwtToken(userDetails);

        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .message("Bearer")
                .build();
    }

    public LogoutResponse logout(HttpServletRequest request) {
        try {
            String jwt = jwtService.parseJwt(request);
            if (jwt != null) {
                // Blacklist the current access token
                jwtService.blacklistToken(jwt, "USER_LOGOUT");
                // Get user from token and revoke all refresh tokens
                String username = jwtService.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                refreshTokenRepository.revokeAllUserTokens(customUserDetails.getId());
                logger.info("User {} logged out successfully", username);
            }
            return LogoutResponse.builder()
                    .message("Logout successful!")
                    .build();
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return LogoutResponse.builder()
                    .message("Logout completed!")
                    .build();
        }
    }

    public LogoutResponse logoutFromAllServices(HttpServletRequest request) {
        try {
            String jwt = jwtService.parseJwt(request);
            if (jwt != null) {
                String username = jwtService.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

                // Revoke all refresh tokens for this user
                refreshTokenRepository.revokeAllUserTokens(customUserDetails.getId());

                // Note: We can't blacklist all access tokens as we don't store them
                // In a production system, you might want to change the user's password salt
                // or use a different approach like token versioning

                logger.info("User {} logged out from all devices", username);
            }
            return LogoutResponse.builder()
                    .message("Logged out from all devices!")
                    .build();
        } catch (Exception e) {
            logger.error("Error during logout from all devices: {}", e.getMessage());
            return LogoutResponse.builder()
                    .message("Error logging out from all devices")
                    .build();
        }
    }

    public RefreshTokenEntity createRefreshToken(Long userId) {
        // Revoke existing tokens for security (optional - depends on your requirements)
        refreshTokenRepository.revokeAllUserTokens(userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(jwtService.generateRefreshTokenValue())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByTokenAndRevokedFalse(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.isExpired() || token.isRevoked()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken());
        }
        return token;
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.revokeToken(token);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.revokeAllUserTokens(userId);
    }

    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
    }
}
