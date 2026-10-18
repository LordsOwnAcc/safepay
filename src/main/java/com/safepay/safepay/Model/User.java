package com.safepay.safepay.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Entity()
@Getter
@Setter
@EnableJpaAuditing
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "full_name",nullable = false)
    private String name;

    @OneToOne(
            mappedBy = "user"
            ,cascade = CascadeType.ALL
            ,
            orphanRemoval = true
            ,optional = true
    )
    private Wallet wallet;


    @Column(name = "password_hash",nullable = false,length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private Role role=Role.USER;

    @Column(name = "enabled" ,nullable = false)
    private boolean enabled=true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

@PrePersist
void onCreate(){
    this.createdAt = Instant.now();
}

    protected User(){

    }
    public User(String email,String password,String name){
    this.name=name;
    this.email=email;
    this.passwordHash=password;
    onCreate();
    }



}
