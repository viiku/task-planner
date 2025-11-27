package com.vikku.authservice.service;

import com.vikku.authservice.model.entity.UserEntity;
import com.vikku.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findActiveByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username or email: " + username));

        return new CustomUserDetails(user);
    }
}
