package com.safepay.safepay.Repository;

import com.safepay.safepay.Model.User;
import com.safepay.safepay.Model.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface walletRepository extends JpaRepository<Wallet, UUID> {


    boolean existsByUser_Id(UUID userId);



    Optional<Wallet> findByUser_Id(UUID userId);

    Optional<Wallet> findByIdAndUser(UUID id, User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout",value = "3000"))
    @Query("select w from Wallet w where w.id=:id")
    Optional<Wallet> findByIdForUpdate(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout",value = "3000"))
    @Query("select w from Wallet w where w.user.id=:id")
    Optional<Wallet> findByUser_IdForUpdate(@Param("id") UUID id);
}
