package com.shopsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDTO {

    private UserInformationDTO userInformationDTO;
    private ResponseCookie responseCookie;
}
