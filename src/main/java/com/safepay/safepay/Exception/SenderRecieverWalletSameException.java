package com.safepay.safepay.Exception;

public class SenderRecieverWalletSameException extends RuntimeException{
    public SenderRecieverWalletSameException(){
        super("Sender and receiver cannot have same wallet");
    }
}
