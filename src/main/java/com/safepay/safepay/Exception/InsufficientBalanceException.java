package com.safepay.safepay.Exception;

public class InsufficientBalanceException extends RuntimeException{
public InsufficientBalanceException(){
    super("Wallet has insufficient balance");
}
}
