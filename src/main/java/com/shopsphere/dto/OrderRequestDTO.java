package com.shopsphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Gateway type is required")
    private String gatewayType;

    private AddressRequestDTO address;
    private String gatewayId;
    private String gatewayStatus;
    private String responseMessage;
}
