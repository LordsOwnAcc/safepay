package com.safepay.safepay.Service;



import com.safepay.safepay.Dto.RegisterRequest;
import com.safepay.safepay.Model.User;
import com.safepay.safepay.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public RegisterRequest.UserSummaryResponse getById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return new RegisterRequest.UserSummaryResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
