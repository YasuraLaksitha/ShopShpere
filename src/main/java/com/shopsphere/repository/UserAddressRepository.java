package com.shopsphere.repository;

import com.shopsphere.entity.UserAddressEntity;
import com.shopsphere.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    Optional<UserAddressEntity> findByStreetNameAndStreetNumber(String StreetName, String StreetNumber);

    List<UserAddressEntity> findByUser(UserEntity user);

}
