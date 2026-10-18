package com.safepay.safepay.Dto;

import com.safepay.safepay.Model.Status;
import com.safepay.safepay.Model.User;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletRequest {

    @NotBlank
    @Pattern(regexp = "^[A-z]{3}$")
    public String curency;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10,fraction = 2)
    public BigDecimal amount;



    @NotNull
    public Status status;

    public record CreateWalletRequest(String currency,BigDecimal amount,Status status){}





}
