package com.vikku.taskplanner.auth.repository;

import com.vikku.taskplanner.auth.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.status = 'ACTIVE'")
    Optional<UserEntity> findActiveByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.status = 'ACTIVE'")
    Optional<UserEntity> findActiveByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE (u.username = :identifier OR u.email = :identifier) AND u.status = 'ACTIVE'")
    Optional<UserEntity> findActiveByUsernameOrEmail(@Param("identifier") String identifier);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") Long userId, @Param("attempts") Integer attempts);

    @Modifying
    @Query("UPDATE UserEntity u SET u.accountLockedUntil = :lockUntil WHERE u.id = :userId")
    void lockAccount(@Param("userId") Long userId, @Param("lockUntil") LocalDateTime lockUntil);

    Optional<UserEntity> findByEmail(String email);
}
