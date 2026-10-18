package com.safepay.safepay.Exception;

public class UserNotFound extends RuntimeException{
   public UserNotFound(){
        super("User does not exist");
    }
}
