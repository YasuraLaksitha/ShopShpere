package com.shopsphere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressDTO {

    @NotBlank(message = "Street number is required")
    @Size(min = 2, message = "Invalid street Number")
    private String streetNumber;

    @NotBlank(message = "Street name is required")
    @Size(min = 2, message = "Invalid street name")
    private String streetName;

    @NotBlank(message = "City is required")
    @Size(min = 3, message = "Invalid city name")
    private String city;

    private String state;

    @NotBlank(message = "country is required")
    @Size(min = 2, message = "Invalid country name")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(min = 4, message = "Invalid Postal code")
    private String postalCode;
}
