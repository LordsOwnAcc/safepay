package com.safepay.safepay.Model;


import com.safepay.safepay.Exception.InsufficientBalanceException;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "wallets" )
@Check(constraints = "balance >= 0")
public class Wallet {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id",unique = true,nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            unique = true,
            nullable = false
    )
    private User user ;

    @Column(name = "currency",nullable = false)
    private String currency;

    @Column(name = "balance",nullable = false,precision = 19,scale = 2)
    private BigDecimal balance=BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private Status status;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "onCreate",nullable = false)
    private Instant createdAt;

    @Column(name = "updatedAt")
    private Instant updatedAt;


    @PrePersist
    private void onCreate(){
        this.createdAt=Instant.now();
    }

    @PreUpdate
    private void setUpdatedAt(){
        this.updatedAt=Instant.now();
    }

protected Wallet(){

}
public Wallet(User user,BigDecimal  balance,String currency , Status status){
        this.user=user;
        this.balance = balance;
        this.status=status;
        this.currency=currency;
}

    public void debit(BigDecimal amount) {
        if (this.status==Status.ACTIVE && amount.compareTo(BigDecimal.ZERO)>0 && amount.compareTo(balance) < 0) { balance = balance.subtract(amount); }
        if (balance.compareTo(amount) < 0) throw new InsufficientBalanceException();
        balance = balance.subtract(amount);
    }
    public void credit(BigDecimal amount) {
        if (this.status==Status.ACTIVE && amount.compareTo(BigDecimal.ZERO)>0 ) {
            balance = balance.add(amount);
        }

    }


}
