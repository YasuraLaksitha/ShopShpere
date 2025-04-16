package com.shopsphere.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.SignInRequestDTO;
import com.shopsphere.dto.SignInResponseDTO;
import com.shopsphere.dto.SignUpRequestDTO;
import com.shopsphere.dto.UserInformationDTO;
import com.shopsphere.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/public/sign-in")
    public ResponseEntity<UserInformationDTO> signIn(@RequestBody final SignInRequestDTO requestDTO,
                                                     final HttpServletResponse response) {
        final SignInResponseDTO responseDTO = userService.handleSignIn(response, requestDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseDTO.getResponseCookie().toString())
                .body(responseDTO.getUserInformationDTO());
    }

    @PostMapping("/public/sign-up")
    public ResponseEntity<String> signUp(@RequestBody final SignUpRequestDTO requestDTO) {
        userService.handleSignUp(requestDTO);
        return new ResponseEntity<>("Sign up successful", HttpStatus.OK);
    }

    @GetMapping("/user/sign-out")
    public ResponseEntity<String> signOut(final HttpServletRequest request) {
        userService.handleSignOut(request);
        return new ResponseEntity<>("Sign out successful", HttpStatus.OK);
    }
}
