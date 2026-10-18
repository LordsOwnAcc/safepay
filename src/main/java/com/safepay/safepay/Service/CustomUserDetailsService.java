package com.safepay.safepay.Service;

import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .map(AuthUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public AuthUser loadUserById(UUID id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .map(AuthUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}