package com.shopsphere.service;

import com.shopsphere.dto.OrderDTO;
import com.shopsphere.dto.OrderRequestDTO;
import org.jetbrains.annotations.NotNull;

public interface OrderService {

    OrderDTO placeOrder(@NotNull OrderRequestDTO orderRequestDTO);
}
