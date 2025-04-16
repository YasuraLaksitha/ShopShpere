package com.shopsphere.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.UserAddressDTO;
import com.shopsphere.entity.UserAddressEntity;
import com.shopsphere.entity.UserEntity;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.UserAddressRepository;
import com.shopsphere.repository.UserRepository;
import com.shopsphere.service.UserAddressService;
import com.shopsphere.utils.AuthUtil;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Override
    public UserAddressDTO save(final UserAddressDTO userAddressDTO) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final UserAddressEntity userAddressEntity = modelMapper.map(userAddressDTO, UserAddressEntity.class);

        userAddressEntity.setUser(loggedInUser);

        final List<UserAddressEntity> addresses = loggedInUser.getAddresses();
        addresses.add(userAddressEntity);
        userRepository.save(loggedInUser);

        final UserAddressEntity savedUserAddress = userAddressRepository.save(userAddressEntity);
        return modelMapper.map(savedUserAddress, UserAddressDTO.class);
    }

    @Override
    public UserAddressDTO retrieveByStreetNameAndStreetNumber(final String street, final String streetNumber) {
        final UserAddressEntity userAddressEntity = userAddressRepository.findByStreetNameAndStreetNumber(
                street,
                streetNumber
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Address", streetNumber + ":" + street, "street name and street " + "number")
        );
        return modelMapper.map(userAddressEntity, UserAddressDTO.class);
    }

    @Override
    public List<UserAddressDTO> retrieveLoggedInUserAddresses() {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        return userAddressRepository.findByUser(loggedInUser).stream().
                map(address ->
                        modelMapper.map(address, UserAddressDTO.class)
                ).toList();
    }

    @Override
    public void removeUserAddress(final String street, final String streetNumber) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final UserAddressEntity userAddressEntity = userAddressRepository.findByStreetNameAndStreetNumber(street,
                        streetNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "user email", loggedInUser.getEmail())
                );
        loggedInUser.getAddresses().remove(userAddressEntity);
        userAddressRepository.delete(userAddressEntity);
    }

    @Override
    public UserAddressDTO update(final UserAddressDTO userAddressDTO) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();

        userAddressRepository.findByUser(loggedInUser).stream().filter(userAddressEntity ->
                userAddressEntity.getStreetName().equals(userAddressDTO.getStreetName())).findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Address", "user email", loggedInUser.getEmail())
        );

        final UserAddressEntity userAddressEntity = modelMapper.map(userAddressDTO, UserAddressEntity.class);
        userAddressEntity.setUser(loggedInUser);

        final List<UserAddressEntity> addresses = loggedInUser.getAddresses();
        addresses.removeIf(address ->
                address.getStreetNumber().equals(userAddressEntity.getStreetNumber()) &&
                        address.getStreetName().equals(userAddressEntity.getStreetName())
        );
        addresses.add(userAddressEntity);

        final UserAddressEntity userAddress = userAddressRepository.save(userAddressEntity);

        return modelMapper.map(userAddress, UserAddressDTO.class);
    }

    @Override
    public UserAddressDTO retrieveByName(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PaginationResponseDTO<UserAddressDTO> retrieveAll
            (final Integer page, final Integer size, final String sortBy, final @NotNull String sortDir) {

        final Sort sortAndOrderBy = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy);

        final Pageable pageable = PageRequest.of(page, size, sortAndOrderBy);

        final Page<UserAddressEntity> entityPage = userAddressRepository.findAll(pageable);

        final Set<UserAddressDTO> addressDTOS = entityPage.getContent().stream().map(entity ->
                        modelMapper.map(entity, UserAddressDTO.class))
                .collect(Collectors.toSet());

        return PaginationResponseDTO.<UserAddressDTO>builder()
                .contentSet(addressDTOS)
                .page(page)
                .sortDir(sortDir)
                .sortBy(sortBy)
                .size(size)
                .isLast(entityPage.isLast())
                .build();
    }
}
