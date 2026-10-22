package com.safepay.safepay.Service;


import com.safepay.safepay.Dto.Requests;
import com.safepay.safepay.Model.EntryType;
import com.safepay.safepay.Model.LedgerEntry;
import com.safepay.safepay.Model.Transfer;
import com.safepay.safepay.Model.Wallet;
import com.safepay.safepay.Repository.LedgerEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;


    public LedgerService(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }



    @Transactional
   public Requests.LedgerEntryResponse create(
            Wallet wal, Transfer transfer, EntryType type,
            BigDecimal amount,BigDecimal balance

            ){
        LedgerEntry ledger = new LedgerEntry(
                wal,transfer,type,amount,balance
        );

        ledgerEntryRepository.saveAndFlush(ledger);
      return new  Requests.LedgerEntryResponse(
              ledger.getId(),ledger.getType()
              ,ledger.getAmount(),ledger.getBalanceAfter(),transfer.getId()
              ,ledger.getCreatedAt()
      );
    }

    public List<Requests.LedgerEntryResponse> getLedger(UUID  walletId){
//UUID id, EntryType type, BigDecimal amount, BigDecimal balanceAfter,
//                                      UUID transferId, Instant createdAt
       List<LedgerEntry> ledgerEntries= ledgerEntryRepository.findByWalletId(walletId,Pageable.ofSize(10)).stream().toList();
        return ledgerEntryRepository
                .findByWalletId(walletId, Pageable.ofSize(10))
                .stream()
                .map(entry -> new Requests.LedgerEntryResponse(
                        entry.getId(),
                        entry.getType(),
                        entry.getAmount(),
                        entry.getBalanceAfter(),
                        entry.getTransfer().getId(),
                        entry.getCreatedAt()
                ))
                .toList();
    }

}
