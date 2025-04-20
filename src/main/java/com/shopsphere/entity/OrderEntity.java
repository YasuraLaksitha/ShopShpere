package com.shopsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shopsphere.utils.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_address_id")
    private UserAddressEntity userAddress;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus orderStatus;
}
