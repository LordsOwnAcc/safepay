package com.safepay.safepay.Controller;

import com.safepay.safepay.Dto.RegisterRequest;
import com.safepay.safepay.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;




    @RestController
    @RequestMapping("/api/v1/auth")
    public class AuthController {

        private final AuthService authService;

        public AuthController(AuthService authService) {
            this.authService = authService;
        }

        @PostMapping("/register")
        @ResponseStatus(HttpStatus.CREATED)
        public RegisterRequest.UserSummaryResponse register(
                @Valid @RequestBody RegisterRequest request) {
            return authService.register(request);
        }

        @PostMapping("/login")
        public RegisterRequest.AuthResponse login(
                @Valid @RequestBody RegisterRequest.LoginRequest request) {
            return authService.login(request);
        }
    }
    // @Valid @RequestBody LoginRequest    -> 200 + AuthResponse


