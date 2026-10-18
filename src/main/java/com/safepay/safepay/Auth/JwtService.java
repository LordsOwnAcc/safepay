package com.safepay.safepay.Auth;

import com.safepay.safepay.Model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final String issuer;
    private final long expirationMinutes;

    public JwtService(@Value("${safepay.jwt.secret}") String secret,
                      @Value("${safepay.jwt.issuer}") String issuer,
                      @Value("${safepay.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));   // throws WeakKeyException if < 256 bits
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(UUID userId, Role role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .issuer(issuer)
                .id(UUID.randomUUID().toString())                      // jti
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofMinutes(expirationMinutes))))
                .signWith(key)                                         // HS256 chosen from key size
                .compact();
    }

    public Claims parse(String token) {                            // throws JwtException if invalid
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(Claims claims) {
        String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Token has no subject");
        }
        return UUID.fromString(subject);
    }
    public long getExpirationSeconds() { return expirationMinutes * 60; }
}