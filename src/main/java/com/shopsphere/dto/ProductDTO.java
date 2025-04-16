package com.shopsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String productName;
    private String productDescription;
    private Integer productQuantity;
    private String image;
    private double productPrice;
    private double productDiscountPrice;
    private double productSpecialPrice;
}
