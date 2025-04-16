package com.shopsphere.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.SignInRequestDTO;
import com.shopsphere.dto.SignInResponseDTO;
import com.shopsphere.dto.SignUpRequestDTO;
import com.shopsphere.dto.UserInformationDTO;
import com.shopsphere.entity.RoleEntity;
import com.shopsphere.entity.UserEntity;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRepository;
import com.shopsphere.service.CookieService;
import com.shopsphere.service.UserService;
import com.shopsphere.utils.AppRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final JwtService jwtService;

    @Value("${spring.application.jwt-cookie}")
    private String jwtCookieName;

    @Override
    @Transactional
    public SignInResponseDTO handleSignIn(final HttpServletResponse response,
                                          final @NotNull SignInRequestDTO request) {
        final UserEntity userEntity = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new BadCredentialsException("Incorrect email or password")
        );
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword(),
                        userEntity.getAuthorities()
                );
        final Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getEmail());
        final Set<String> roles =
                userEntity.getRoles().stream().map(r -> r.getRoleName().name()).collect(Collectors.toSet());

        final String generatedToken = jwtService.generateAccessToken(userDetails);
        final ResponseCookie responseCookie = cookieService.generateResponseCookieWithValue(
                jwtCookieName,
                generatedToken
        );

        final UserInformationDTO informationDTO = UserInformationDTO
                .builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .roles(roles)
                .build();

        return SignInResponseDTO.builder()
                .userInformationDTO(informationDTO)
                .responseCookie(responseCookie)
                .build();
    }

    @Override
    public void handleSignUp(final @NotNull SignUpRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DataIntegrityViolationException("Email already exists");
        else if (userRepository.existsByUsername(request.getUsername()))
            throw new DataIntegrityViolationException("Username already exists");

        final List<RoleEntity> roleSet = new ArrayList<>();

        if (request.getRoles().isEmpty()) {
            final RoleEntity userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow();
            roleSet.add(userRole);
        }

        request.getRoles().forEach(role -> {
            switch (role) {
                case "admin":
                    roleSet.add(roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow());
                    break;

                case "seller":
                    roleSet.add(roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow());
                    break;

                case "user":
                    roleSet.add(roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow());
                    break;

                default:
                    throw new ResourceNotFoundException("Role", "roleName", role);
            }

            final UserEntity userEntity = new UserEntity();
            userEntity.setEmail(request.getEmail());
            userEntity.setUsername(request.getUsername());
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            userEntity.setRoles(roleSet);
            userRepository.save(userEntity);
        });
    }

    @Override
    public void handleSignOut(final HttpServletRequest request) {
        cookieService.clearCookieByName(jwtCookieName, request);
    }
}
