package com.shopsphere.service;

import com.shopsphere.dto.PaginationResponseDTO;

public interface CommonService<T> {
    T save(T t);

    T update(T t);

    T retrieveByName(String name);

    PaginationResponseDTO<T> retrieveAll(Integer page, Integer size, String sortBy, String sortDir);
}
