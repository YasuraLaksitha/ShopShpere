package com.shopsphere.controller;

import com.shopsphere.config.ApplicationDefaultConstants;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.UserAddressDTO;
import com.shopsphere.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {

    private final UserAddressService userAddressService;

    @PostMapping("/user")
    public ResponseEntity<UserAddressDTO> postUserAddress(@Valid @RequestBody UserAddressDTO userAddressDTO) {
        return new ResponseEntity<>(userAddressService.save(userAddressDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserAddressDTO>> getUserAddress() {
        return ResponseEntity.ok(userAddressService.retrieveLoggedInUserAddresses());
    }

    @PutMapping("/user")
    public ResponseEntity<UserAddressDTO> put(@Valid @RequestBody UserAddressDTO userAddressDTO) {
        return ResponseEntity.ok(userAddressService.update(userAddressDTO));
    }

    @DeleteMapping("/user/streets/{streetName}/street-numbers/{streetNumber}")
    public ResponseEntity<String> deleteByLoggedInUser
            (@PathVariable final String streetName, @PathVariable final String streetNumber) {
        userAddressService.removeUserAddress(streetName, streetNumber);
        return ResponseEntity.ok("User address deleted successfully.");
    }

    @GetMapping("/admin/streets/{streetName}/street-numbers/{streetNumber}")
    public ResponseEntity<UserAddressDTO> getByStreetNameAndStreetNumber
            (@PathVariable String streetName, @PathVariable String streetNumber) {
        return ResponseEntity.ok(userAddressService.retrieveByStreetNameAndStreetNumber(streetName, streetNumber));
    }

    @GetMapping("/admin/addresses")
    public ResponseEntity<PaginationResponseDTO<UserAddressDTO>> getAllAddresses(
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_SIZE, required = false) Integer size,
            @RequestParam(defaultValue = ApplicationDefaultConstants.SORT_USER_ADDRESSES_BY, required = false) String sortBy,
            @RequestParam(defaultValue = ApplicationDefaultConstants.SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(userAddressService.retrieveAll(page, size, sortBy, sortDir));
    }
}
