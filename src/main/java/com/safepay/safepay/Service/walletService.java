package com.safepay.safepay.Service;

import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Dto.Requests;
import com.safepay.safepay.Exception.UserNotFound;
import com.safepay.safepay.Exception.WalletAlreadyExists;
import com.safepay.safepay.Exception.WalletNotFoundException;
import com.safepay.safepay.Model.LedgerEntry;
import com.safepay.safepay.Model.Status;
import com.safepay.safepay.Model.User;
import com.safepay.safepay.Model.Wallet;
import com.safepay.safepay.Repository.UserRepository;
import com.safepay.safepay.Repository.walletRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class walletService {

    private final UserRepository userRepo;
    private final transferService transferService;
    private final walletRepository repo;
    private  final LedgerService ledgerService;


    walletService(walletRepository repo, UserRepository  userRepo, transferService transferService, LedgerService ledgerService){
        this.userRepo=userRepo;
        this.repo=repo;
        this.transferService = transferService;
        this.ledgerService = ledgerService;
    }

    @Transactional()
    @Cacheable(cacheNames = "wallets",key = "#userId")
    public Requests.WalletResponse  getMyWallet(UUID userId){

        System.out.println("Fetching wallet from database");
            if (!repo.existsByUser_Id (userId)) throw  new WalletNotFoundException();

            Wallet wal = repo.findByUser_Id(userId).orElseThrow(
                    WalletNotFoundException::new
            );

            return new Requests.WalletResponse(
                    wal.getId(),
                    wal.getCurrency()
                    ,wal.getBalance()
                    ,wal.getStatus()
                    ,wal.getCreatedAt()
            );


    }


@Transactional
    public Requests.WalletResponse createWallet(UUID uuid
){
     final User user=userRepo.findById(uuid).orElseThrow(
             UserNotFound::new

     );


            if (repo.existsByUser_Id(uuid)) throw (new WalletAlreadyExists());



            Wallet wal = new Wallet(user, BigDecimal.ZERO, "INR", Status.ACTIVE);
                repo.saveAndFlush(wal);
                return new Requests.WalletResponse(
                         wal.getId(),
                         wal.getCurrency()
                        ,wal.getBalance()
                        ,wal.getStatus()
                        ,wal.getCreatedAt()
                );



    }
    @Transactional
    public List<Requests.LedgerEntryResponse> transaction(UUID userID){
       Wallet wallet= repo.findByUser_Id(userID).orElseThrow(
               WalletNotFoundException::new
       );
        return ledgerService.getLedger(wallet.getId());

    }

    @Transactional
    public Requests.DepositResponse deposit(
            AuthUser user,
            Requests.DepositRequest request
    ) {
        Wallet wal = repo.findByUser_IdForUpdate(user.getId())
                .orElseThrow(WalletNotFoundException::new);

        Requests.TransferRequest transferRequest =
                new Requests.TransferRequest(
                        null,
                        request.amount(),
                        request.note(),
                        request.key()
                );

        transferService.initiateDeposit(user.getId(), transferRequest);

        // Reload/read the current balance from the managed wallet.
        return new Requests.DepositResponse(
                wal.getId(),
                Instant.now(),
                request.amount(),
                wal.getBalance()
        );
    }
}
