package com.safepay.safepay.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleWalletNotFound(
            WalletNotFoundException ex) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", 404,
                "error", "WALLET_NOT_FOUND",
                "message", ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleWalletExist(
            WalletAlreadyExists ex
    ){
        Map<String,Object> body= Map.of(
                "timeStamp",Instant.now(),
                "status",409,
                "error","WALLET_ALREADY_EXISTS",
                "message",ex.getMessage()



        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);

    }




}
