package com.shopsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<T> contentSet;
}
