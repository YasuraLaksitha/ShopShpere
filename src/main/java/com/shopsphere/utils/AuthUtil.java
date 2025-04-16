package com.shopsphere.utils;

import lombok.RequiredArgsConstructor;
import com.shopsphere.entity.UserEntity;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    public UserEntity getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("user", userEmail, "email"
                ));
    }
}
