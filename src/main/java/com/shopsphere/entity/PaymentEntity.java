package com.shopsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shopsphere.utils.PaymentGateWayTypes;
import com.shopsphere.utils.PaymentMethods;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentGateWayTypes type;

    @Enumerated(EnumType.STRING)
    private PaymentMethods method;

    @OneToOne(mappedBy = "payment")
    private OrderEntity order;

    private String gateWayId;
    private String gateWayStatus;
    private String gateWayResponseMessage;
}
