package com.vikku.taskplanner.auth.repository;

import com.vikku.taskplanner.auth.model.entity.BlackListedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlackListedTokenEntity, Long> {
    boolean existsByTokenHash(String tokenHash);

    @Modifying
    @Query("DELETE FROM BlackListedTokenEntity bt WHERE bt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
}
