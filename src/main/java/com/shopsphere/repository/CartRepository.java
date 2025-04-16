package com.shopsphere.repository;

import com.shopsphere.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findCartByUserEmail(final String email);
}
