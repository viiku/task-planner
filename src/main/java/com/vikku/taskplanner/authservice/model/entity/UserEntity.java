package com.vikku.taskplanner.authservice.model.entity;

import com.vikku.taskplanner.authservice.model.enums.UserRole;
import com.vikku.taskplanner.authservice.model.enums.UserStatus;
import com.vikku.taskplanner.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_username", columnList = "username")
        }
)
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

}
