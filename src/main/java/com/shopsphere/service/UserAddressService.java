package com.shopsphere.service;

import com.shopsphere.dto.UserAddressDTO;

import java.util.List;

public interface UserAddressService extends CommonService<UserAddressDTO> {

    UserAddressDTO retrieveByStreetNameAndStreetNumber(String street, String streetNumber);

    List<UserAddressDTO> retrieveLoggedInUserAddresses();

    void removeUserAddress(String street, String streetNumber);
}
