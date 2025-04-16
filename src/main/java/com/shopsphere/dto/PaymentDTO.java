package com.shopsphere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shopsphere.utils.PaymentGateWayTypes;
import com.shopsphere.utils.PaymentMethods;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @NotNull(message = "Payment gateway type is required")
    private PaymentGateWayTypes type;

    @NotNull(message = "Payment method is required")
    private PaymentMethods method;

    @NotBlank(message = "Gateway transaction ID is required")
    private String gateWayId;

    @NotBlank(message = "Gateway status is required")
    private String gateWayStatus;

    @NotBlank(message = "Gateway response message is required")
    private String gateWayResponseMessage;
}
