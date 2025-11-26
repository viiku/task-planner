package com.vikku.taskplanner.authservice.service;

import com.vikku.taskplanner.authservice.model.entity.BlackListedTokenEntity;
import com.vikku.taskplanner.authservice.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(CustomUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername(), jwtExpirationMs);
    }

    public String generateRefreshTokenValue() {
        return generateTokenFromUsername(UUID.randomUUID().toString(), jwtRefreshExpirationMs);
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername(), jwtRefreshExpirationMs);
    }

    private String generateTokenFromUsername(String username, int expirationMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getTokenId(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    public Date getExpirationDateFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            if(isTokenBlacklisted(jwtToken)) {
                logger.error("Token is blacklisted");
                return false;
            }

            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean isTokenBlacklisted(String token) {
        String tokenHash = generateTokenHash(token);
        return blacklistedTokenRepository.existsByTokenHash(tokenHash);
    }

    public void blacklistToken(String token, String reason) {
        try {
            Date expirationDate = getExpirationDateFromJwtToken(token);
            String tokenHash = generateTokenHash(token);

            BlackListedTokenEntity blacklistedToken = BlackListedTokenEntity.builder()
                    .tokenHash(tokenHash)
                    .expiryDate(expirationDate.toInstant())
                    .reason(reason)
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        } catch (Exception e) {
            logger.error("Error blacklisting token: {}", e.getMessage());
        }
    }

    private String generateTokenHash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
