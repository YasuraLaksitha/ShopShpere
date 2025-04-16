package com.shopsphere.repository;

import com.shopsphere.entity.CategoryEntity;
import com.shopsphere.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    Optional<ProductEntity> findByProductName(String productName);
    Page<ProductEntity> findAllByCategoryAndUnavailableFalseOrderByProductPriceAsc(final CategoryEntity category, final Pageable pageable);
    Page<ProductEntity> findAllByProductNameLikeIgnoreCaseAndUnavailableFalse(final String productName, final Pageable pageable);
}
