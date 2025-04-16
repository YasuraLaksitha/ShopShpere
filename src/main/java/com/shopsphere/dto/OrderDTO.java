package com.shopsphere.dto;

import com.shopsphere.utils.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "Order items cannot be empty")
    private List<@Valid OrderItemDTO> orderItems;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    private LocalDateTime orderDate;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be zero or positive")
    private Double totalAmount;

    @NotNull(message = "Shipping address is required")
    private UserAddressDTO address;

    @NotNull(message = "Payment information is required")
    private PaymentDTO payment;
}
