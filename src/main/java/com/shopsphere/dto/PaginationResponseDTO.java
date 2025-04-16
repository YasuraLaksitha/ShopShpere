package com.shopsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationResponseDTO<T> {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDir;
    private boolean isLast;
    private Set<T> contentSet;
}
