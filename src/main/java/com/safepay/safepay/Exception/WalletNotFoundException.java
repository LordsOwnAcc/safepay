package com.safepay.safepay.Exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super("Wallet not found");
    }
}