package com.shopsphere.repository;

import com.shopsphere.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);
}
