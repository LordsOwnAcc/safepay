package com.safepay.safepay.Repository;

import com.safepay.safepay.Model.EntryType;
import com.safepay.safepay.Model.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    Page<LedgerEntry> findByWalletId(UUID walletId, Pageable pageable);

    @Query("select coalesce(sum(e.amount), 0) from LedgerEntry e where e.wallet.id = :walletId and e.type = :type")
    BigDecimal sumByWalletAndType(@Param("walletId") UUID walletId, @Param("type") EntryType type);
}