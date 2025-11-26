package com.vikku.taskplanner.authservice.repository;

import com.vikku.taskplanner.authservice.model.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.user.id = :userId AND rt.revoked = false")
    List<RefreshTokenEntity> findActiveTokensByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.user.id = :userId")
    void revokeAllUserTokens(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") Instant now);

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.token = :token")
    void revokeToken(@Param("token") String token);
}
