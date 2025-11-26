package com.vikku.taskplanner.authservice.model.entity;

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
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_token", columnList = "token"),
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_expiry_date", columnList = "expiry_date")
        }
)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "is_revoked")
    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
