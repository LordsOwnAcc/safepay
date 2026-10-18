package com.safepay.safepay.Service;

import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Dto.RegisterRequest;
import com.safepay.safepay.Dto.Requests;
import com.safepay.safepay.Exception.WalletNotFoundException;
import com.safepay.safepay.Model.*;
import com.safepay.safepay.Repository.TransferRepository;
import com.safepay.safepay.Repository.UserRepository;
import com.safepay.safepay.Repository.walletRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class transferService {
    private final walletRepository repo;
    private final TransferRepository trans_repo;

    private final LedgerService ledgerService;



    public transferService(walletRepository repo, TransferRepository transRepo, LedgerService ledgerService) {
        this.repo = repo;
        trans_repo = transRepo;
        this.ledgerService = ledgerService;


    }
    public Requests.TransferResponse getResponse(UUID user_id, String idemKey){

      Transfer trans = trans_repo.findByInitiatedBy_IdAndIdempotencyKey(user_id,idemKey).orElseThrow(
                WalletNotFoundException::new
        );
        return new  Requests.TransferResponse(
                trans.getId(),trans.getType(),trans.getFromWallet().getId(),trans.getToWallet().getId(),trans.getAmount(),trans.getCurrency(),trans.getStatus(),trans.getNote(),trans.getCreatedAt()
        );

    }

    @Transactional
    public Requests.TransferResponse initiateTransfer(
            UUID  userID,
            Requests.TransferRequest request
    ) {
        Wallet toWallet = repo.findByIdForUpdate(request.toWalletId())
                .orElseThrow(WalletNotFoundException::new);
        Wallet fromWallet = repo.findByUser_IdForUpdate(userID).orElseThrow(
                WalletNotFoundException::new
        );

        toWallet.credit(request.amount());
        fromWallet.debit(request.amount());


        Transfer transfer = new Transfer(
                TransferType.WALLET_TRANSFER
                , fromWallet
                , toWallet
                , request.amount()
                , "INR", fromWallet.getUser(), request.key()
        );
        trans_repo.save(transfer);
        ledgerService.create(
                fromWallet,transfer, EntryType.DEBIT,request.amount(),fromWallet.getBalance()
        );
        ledgerService.create(toWallet,transfer,EntryType.CREDIT,request.amount(),toWallet.getBalance());


        return new Requests.TransferResponse(
                transfer.getId()
                , transfer.getType(), transfer.getFromWallet().getId(), transfer.getToWallet().getId(), transfer.getAmount(), transfer.getCurrency(), transfer.getStatus(), transfer.getNote()
                , transfer.getCreatedAt()
        );

    }

    @Transactional
    public Requests.TransferResponse initiateDeposit(
            UUID userID,
            Requests.TransferRequest request
    ) {
        Wallet toWallet = repo.findByUser_IdForUpdate(userID)
                .orElseThrow(WalletNotFoundException::new);

        BigDecimal amount = request.amount();

        // Validate the amount before modifying the wallet.
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        // Update the wallet balance exactly once.
        toWallet.credit(amount);

        Transfer transfer = new Transfer(
                TransferType.DEPOSIT,
                null,
                toWallet,
                amount,
                "INR",
                toWallet.getUser(),
                request.key()
        );

        trans_repo.saveAndFlush(transfer);

        // Record the actual resulting balance, not just the deposit amount.
        ledgerService.create(
                toWallet,
                transfer,
                EntryType.CREDIT,
                amount,
                toWallet.getBalance()
        );

        return new Requests.TransferResponse(
                transfer.getId(),
                transfer.getType(),
                null,
                transfer.getToWallet().getId(),
                transfer.getAmount(),
                transfer.getCurrency(),
                transfer.getStatus(),
                transfer.getNote(),
                transfer.getCreatedAt()
        );
    }




}
