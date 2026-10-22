package com.safepay.safepay.Controller;

import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Dto.Requests;
import com.safepay.safepay.Model.LedgerEntry;
import com.safepay.safepay.Service.transferService;
import com.safepay.safepay.Service.walletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/wallet")
public class WalletController {
private final walletService wallet;
private final transferService transfer;

WalletController(walletService ser, transferService transfer){
    wallet=ser;
    this.transfer = transfer;
}

@PostMapping("/create")
 public ResponseEntity<Requests.WalletResponse> createWallet(@AuthenticationPrincipal AuthUser principal){
    UUID id =principal.getId();
   Requests.WalletResponse response= wallet.createWallet(id);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(response)
            ;
}

@PostMapping("/deposit")
    public ResponseEntity<Requests.DepositResponse> deposit(@AuthenticationPrincipal AuthUser user,@Valid @RequestBody Requests.DepositRequest req){
 Requests.DepositResponse response= wallet.deposit(user,req);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(response);
}

@GetMapping("/myWallet")
    public ResponseEntity<Requests.WalletResponse> getWallet(@AuthenticationPrincipal AuthUser user){
    Requests.WalletResponse re=wallet.getMyWallet(user.getId());
    return ResponseEntity.ok(re);

}

@GetMapping("/transaction")
    public ResponseEntity<List<Requests.LedgerEntryResponse>> getTransaction(@AuthenticationPrincipal AuthUser user){
    List<Requests.LedgerEntryResponse> entru = wallet.transaction(user.getId());
    return ResponseEntity.ok(entru);
}


}
