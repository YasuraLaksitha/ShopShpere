package com.shopsphere.controller;

import lombok.RequiredArgsConstructor;
import com.shopsphere.config.ApplicationDefaultConstants;
import com.shopsphere.dto.CartDTO;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/user/product/{productName}/quantity/{quantity}")
    public ResponseEntity<CartDTO> post(
            @PathVariable("productName") final String productName,
            @PathVariable("quantity") final Integer quantity
    ) {
        return new ResponseEntity<>(cartService.addCartItem(productName, quantity), HttpStatus.CREATED);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<CartDTO> getUserCart(@PathVariable final String email) {
        return ResponseEntity.ok(cartService.retrieveUserCartByEmail(email));
    }

    @PutMapping("/user/product/{productName}/quantity/{quantity}/operation/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @PathVariable final String productName,
            @PathVariable final Integer quantity,
            @PathVariable(required = false) final String operation
    ) {
        return ResponseEntity.ok(cartService.updateProductQuantityInCart(productName, quantity, operation));
    }

    @DeleteMapping("/user/product/{productName}")
    public ResponseEntity<String> removeProductByName(@PathVariable final String productName) {
        cartService.removeCartItemsByProductFromCart(productName);
        return ResponseEntity.ok("Product removed successfully");
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<PaginationResponseDTO<CartDTO>> getCarts(
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_SIZE, required = false) Integer size
    ) {
        return ResponseEntity.ok(cartService.retrieveAllCarts(page, size));
    }
}
