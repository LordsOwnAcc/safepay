package com.safepay.safepay.Exception;

public class WalletAlreadyExists extends RuntimeException{

    public WalletAlreadyExists(){
        super("Wallet already exists");
    }

}
