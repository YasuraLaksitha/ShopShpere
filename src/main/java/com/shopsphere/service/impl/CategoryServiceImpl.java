package com.shopsphere.service.impl;

import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.CategoryDTO;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.entity.CategoryEntity;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.CategoryRepository;
import com.shopsphere.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;

    @Override
    public CategoryDTO save(final CategoryDTO categoryDTO) {
        try {
            final CategoryEntity saved = repository.save(mapper.map(categoryDTO, CategoryEntity.class));
            return mapper.map(saved, CategoryDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("category already exists");
        }
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        if (!repository.existsByName(categoryDTO.getName()))
            throw new ResourceNotFoundException("CategoryModel", "name", categoryDTO.getName());
        return save(categoryDTO);
    }

    @Override
    public CategoryDTO retrieveByName(String name) {
        final CategoryEntity categoryEntity = repository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("CategoryModel", "name", name)
        );
        return mapper.map(categoryEntity, CategoryDTO.class);
    }

    @Override
    public PaginationResponseDTO<CategoryDTO> retrieveAll(Integer page, Integer size, String sortBy, String sortDir) {

        final Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        final Pageable pageable = PageRequest.of(page, size, sortByAndOrder);
        final Page<CategoryEntity> categoryEntities = repository.findAll(pageable);

        return PaginationResponseDTO.<CategoryDTO>builder()
                .contentSet(categoryEntities.getContent().stream().map(
                        categoryEntity -> mapper.map(categoryEntity, CategoryDTO.class)
                ).collect(Collectors.toUnmodifiableSet()))
                .page(page)
                .sortDir(sortDir)
                .sortBy(sortBy)
                .size(size)
                .isLast(categoryEntities.isLast())
                .build();
    }
}
