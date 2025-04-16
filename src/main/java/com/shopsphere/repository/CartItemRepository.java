package com.shopsphere.repository;

import com.shopsphere.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("SELECT c FROM CartItemEntity c WHERE c.product.productName = :productName AND c.cart.user.email = :email ")
    Optional<CartItemEntity> findByProductNameAndUserEmail(@Param("productName") String productName,
                                                           @Param("email") String email);
}
