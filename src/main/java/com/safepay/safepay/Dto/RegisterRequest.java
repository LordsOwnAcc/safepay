package com.safepay.safepay.Dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public class RegisterRequest {
    @NotBlank
   public String fullname;
    
    @NotBlank
    @Email
   public String  email;

    @NotBlank
    @Size(min = 8,max = 72)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "must contain upper case, lower case and a digit")
   public String password;

    public record LoginRequest(@NotBlank @Email String email,@NotBlank String password){}
    public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds) {}

    public record UserSummaryResponse(UUID id, String fullName, String email) {}
    
}
