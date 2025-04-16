package com.shopsphere.repository;

import com.shopsphere.entity.RoleEntity;
import com.shopsphere.utils.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByRoleName(final AppRole roleName);
}
