package com.shopsphere.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import com.shopsphere.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.application.security.jwt-expiration}")
    private String expiration;

    @Value("${spring.application.security.jwt-secret}")
    private String secret;

    public String generateAccessToken(final UserDetails userDetails) {
        return JWTUtil.generateToken(userDetails, secret, Long.valueOf(expiration));
    }

    public String getUsername(final String token) {
        return parseToken(token).getSubject();
    }

    private Claims parseToken(final String token) {
        return JWTUtil.parseToken(token, secret);
    }

    public String[] getRolesFromToken(final String token) {
        return parseToken(token).get("roles", String[].class);
    }
}
