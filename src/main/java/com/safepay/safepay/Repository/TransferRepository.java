package com.safepay.safepay.Repository;

import com.safepay.safepay.Model.Transfer;
import com.safepay.safepay.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    Optional<Transfer> findByInitiatedBy_IdAndIdempotencyKey(UUID initiatedById, String idempotencyKey);
}
