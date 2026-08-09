package com.esign.platform.identity.auth.config;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.esign.platform.identity.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expiration) {

        this.secretKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secret)
                );

        this.expiration = expiration;
    }

    /**
     * Generate JWT for authenticated user.
     */
    public String generateToken(User user) {

        Date issuedAt = new Date();
        Date expirationDate =
                new Date(
                        issuedAt.getTime() + expiration
                );

        return Jwts.builder()

                /*
                 * Subject = User ID
                 */
                .subject(
                        user.getId().toString()
                )

                /*
                 * Useful identity claims
                 */
                .claim(
                        "email",
                        user.getEmail()
                )

                .claim(
                        "roleCode",
                        user.getRole().getRoleCode()
                )

                .claim(
                        "roleName",
                        user.getRole().getRoleName()
                )

                .claim(
                        "roleType",
                        user.getRole().getRoleType().name()
                )

                .claim(
                        "employeeId",
                        user.getEmployee().getId().toString()
                )

                .claim(
                        "organizationId",
                        user.getEmployee().getOrganizationId() != null
                                ? user.getEmployee()
                                        .getOrganizationId()
                                        .toString()
                                : null
                )

                .issuedAt(issuedAt)
                .expiration(expirationDate)

                .signWith(secretKey)

                .compact();
    }

    /**
     * Extract User ID from JWT.
     */
    public UUID extractUserId(String token) {

        String subject =
                extractAllClaims(token)
                        .getSubject();

        return UUID.fromString(subject);
    }

    /**
     * Validate JWT.
     */
    public boolean isTokenValid(
            String token,
            User user) {

        try {

            UUID userId =
                    extractUserId(token);

            return userId.equals(user.getId())
                    && !isTokenExpired(token);

        } catch (Exception ex) {

            return false;
        }
    }

    /**
     * Check expiration.
     */
    private boolean isTokenExpired(String token) {

        Date expirationDate =
                extractAllClaims(token)
                        .getExpiration();

        return expirationDate.before(
                new Date()
        );
    }

    /**
     * Parse JWT claims.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}