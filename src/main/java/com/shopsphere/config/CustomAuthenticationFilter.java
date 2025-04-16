package com.shopsphere.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.shopsphere.service.CookieService;
import com.shopsphere.service.impl.JwtService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CookieService cookieService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Value("${spring.application.jwt-cookie}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal
            (
                    final @NotNull HttpServletRequest request,
                    final @NotNull HttpServletResponse response,
                    final @NotNull FilterChain filterChain
            ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final Cookie jwtCookie = cookieService.retrieveCookieByName(jwtCookieName, request);

            if (jwtCookie != null) {
                final String token = jwtCookie.getValue();

                final UserDetails userDetails =
                        userDetailsService.loadUserByUsername(jwtService.getUsername(token));

                final UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            doFilter(request, response, filterChain);
        }
    }
}
