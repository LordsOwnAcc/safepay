package com.safepay.safepay.Dto;

import com.safepay.safepay.Model.EntryType;
import com.safepay.safepay.Model.Status;
import com.safepay.safepay.Model.TransferStatus;
import com.safepay.safepay.Model.TransferType;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Requests {
    public record CreateWalletRequest(@NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency) {}

    public record DepositRequest(
            @NotNull @DecimalMin("0.01") @Digits(integer = 15, fraction = 2) BigDecimal amount
      ,@Size(max = 140) String note
            ,
            @NotNull String key) {}


    public record DepositResponse(
            @NotNull UUID uuid,
            @NotNull Instant timestamp,
            @NotNull @DecimalMin("0.01")@Digits(integer = 15,fraction = 2)
            BigDecimal amountDeposited
            ,
  @NotNull @DecimalMin("0.01")@Digits(integer = 15,fraction = 2) BigDecimal balance
            ){};
    public record TransferRequest(


            @Nullable UUID toWalletId,
            @NotNull @DecimalMin("0.01") @Digits(integer = 15, fraction = 2) BigDecimal amount,
            @Size(max = 140) String note
            ,
            @NotNull String key
    ) {}

    public record UpdateWalletStatusRequest(@NotNull Status status) {}

    // responses
    public record WalletResponse(UUID id, String currency, BigDecimal balance, Status status, Instant createdAt) {}
    public record TransferResponse(UUID transferId, TransferType type,@Nullable UUID fromWalletId, UUID toWalletId,
                                   BigDecimal amount, String currency, TransferStatus status, String note, Instant createdAt) {}
    public record LedgerEntryResponse(UUID id, EntryType type, BigDecimal amount, BigDecimal balanceAfter,
                                      UUID transferId, Instant createdAt) {}
    public record ReconciliationResponse(UUID walletId, BigDecimal storedBalance, BigDecimal ledgerBalance, boolean consistent) {}
//    public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
//        public static <T> PageResponse<T> from(Page<T> p) {
//
//        }
//    }
}
// requests


