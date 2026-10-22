package com.safepay.safepay.Controller;


import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Dto.Requests;
import com.safepay.safepay.Service.transferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {
    private final transferService service;

    public TransferController(transferService service) {
        this.service = service;
    }

    @PostMapping("/toWallet")
    public ResponseEntity<Requests.TransferResponse> toWallet(@AuthenticationPrincipal AuthUser user,@Valid @RequestBody Requests.TransferRequest  request){
       Requests.TransferResponse response= service.initiateTransfer(user.getId(),request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }
}
