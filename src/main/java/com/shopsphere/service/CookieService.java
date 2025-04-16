package com.shopsphere.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface CookieService {
    Cookie retrieveCookieByName(String name, HttpServletRequest request);

    ResponseCookie generateResponseCookieWithValue(String name, String value);

    void clearCookieByName(String name, HttpServletRequest request);
}
