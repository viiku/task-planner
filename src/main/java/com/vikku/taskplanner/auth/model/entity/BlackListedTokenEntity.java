package com.vikku.taskplanner.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "blacklisted_tokens",
        indexes = {
                @Index(name = "idx_token_hash", columnList = "token_hash"),
                @Index(name = "idx_expiry_date", columnList = "expiry_date")
        }
)
public class BlackListedTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash; // SHA-256 hash of the token for space efficiency

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "blacklisted_at", nullable = false)
    private Instant blacklistedAt;

    @Column(name = "reason", length = 100)
    private String reason;

    @PrePersist
    protected void onCreate() {
        blacklistedAt = Instant.now();
    }
}
