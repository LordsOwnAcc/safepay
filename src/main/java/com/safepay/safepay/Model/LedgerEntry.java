package com.safepay.safepay.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entry",indexes = @Index(
        name = "idx_ledger_wallet_created"
        ,columnList = "wallet_id, created_at"
))

@Immutable
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;


    protected LedgerEntry() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }


    public UUID getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Transfer getTransfer() {
        return transfer;
    }


    public EntryType getType() {
        return type;
    }



    public BigDecimal getAmount() {
        return amount;
    }



    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }



    public Instant getCreatedAt() {
        return createdAt;
    }
    public LedgerEntry(
            Wallet wallet,
            Transfer transfer,
            EntryType type,
            BigDecimal amount,
            BigDecimal balanceAfter
    ) {
        this.wallet = wallet;
        this.transfer = transfer;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

}
