package com.shopsphere.service;

import com.shopsphere.dto.CartDTO;
import com.shopsphere.dto.PaginationResponseDTO;

public interface CartService {

    CartDTO addCartItem(String productName, Integer quantity);

    PaginationResponseDTO<CartDTO> retrieveAllCarts(final Integer page, final Integer size);

    CartDTO retrieveUserCartByEmail(String userEmail);

    CartDTO updateProductQuantityInCart(String productName, Integer quantity, String operation);

    void removeCartItemsByProductFromCart(String productName);

    void deleteProductFromCarts(String productName);
}
