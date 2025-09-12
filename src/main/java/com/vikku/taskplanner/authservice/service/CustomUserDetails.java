package com.vikku.taskplanner.authservice.service;

import com.vikku.taskplanner.authservice.model.entity.UserEntity;
import com.vikku.taskplanner.authservice.model.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // or add logic if you track expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.getAccountLockedUntil() == null ||
                userEntity.getAccountLockedUntil().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // or add logic if you track credential expiry
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getStatus() == UserStatus.ACTIVE;
    }

    // Additional getters to expose useful UserEntity fields
    public Long getId() {
        return userEntity.getId();
    }

    public String getEmail() {
        return userEntity.getEmail();
    }

    public String getFirstName() {
        return userEntity.getFirstName();
    }

    public String getLastName() {
        return userEntity.getLastName();
    }

    public String getFullName() {
        return (userEntity.getFirstName() != null ? userEntity.getFirstName() + " " : "") +
                (userEntity.getLastName() != null ? userEntity.getLastName() : "");
    }

    public UserEntity getUser() {
        return userEntity;
    }
}
