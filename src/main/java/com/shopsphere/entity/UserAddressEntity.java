package com.shopsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_address")
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String streetNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "userAddress")
    private List<OrderEntity> order = new ArrayList<>();
}
