package com.shopsphere.controller;

import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.OrderDTO;
import com.shopsphere.dto.OrderRequestDTO;
import com.shopsphere.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/user")
    public ResponseEntity<OrderDTO> postNewOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return new ResponseEntity<>(orderService.placeOrder(orderRequestDTO), HttpStatus.CREATED);
    }
}
