package com.shopsphere.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.shopsphere.dto.SignInRequestDTO;
import com.shopsphere.dto.SignInResponseDTO;
import com.shopsphere.dto.SignUpRequestDTO;

public interface UserService {
    SignInResponseDTO handleSignIn(final HttpServletResponse response, final SignInRequestDTO request);
    void handleSignUp(final SignUpRequestDTO request);
    void handleSignOut(final HttpServletRequest request);
}
