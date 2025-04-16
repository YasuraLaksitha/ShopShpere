package com.shopsphere.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.shopsphere.exceptions.InvalidJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

public final class JWTUtil {

    public static String generateToken(final UserDetails userDetails, final String secret, final Long expiration) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .signWith(getSignInKey(secret))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    private static @NotNull SecretKey getSignInKey(final String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public static boolean isValidateToken(final String token, final String secret) {
        return !parseToken(token, secret).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public static Claims parseToken(final String token, final String secret) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException(e.getMessage());
        }
    }
}
