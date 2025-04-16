package com.shopsphere.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import com.shopsphere.service.CookieService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Cookie retrieveCookieByName(final String name, final HttpServletRequest request) {
        return WebUtils.getCookie(request, name);
    }

    @Override
    public ResponseCookie generateResponseCookieWithValue(final String name, final String value) {
        return ResponseCookie.from(name, value)
                .path("/api")
                .maxAge(3600 * 24)
                .httpOnly(true)
                .build();
    }

    @Override
    public void clearCookieByName(final String name, final HttpServletRequest request) {
        final Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            cookie.setValue("");
            return;
        }
        throw new InvalidCookieException("Unable to find cookie");
    }
}
