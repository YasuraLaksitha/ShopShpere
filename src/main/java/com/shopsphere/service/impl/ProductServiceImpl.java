package com.shopsphere.service.impl;

import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.ProductDTO;
import com.shopsphere.entity.CategoryEntity;
import com.shopsphere.entity.ProductEntity;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.CategoryRepository;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.CartService;
import com.shopsphere.service.FileService;
import com.shopsphere.service.ProductService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final CartService cartService;

    @Value("${spring.application.image.dir}")
    private String imageDirName;

    @Value("${spring.application.image.base-url}")
    private String imageBaseUrl;

    @Override
    public ProductDTO save(ProductDTO productDTO, String categoryName) {

        final CategoryEntity category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("category", "name", categoryName));
        final ProductEntity productEntity = mapper.map(productDTO, ProductEntity.class);

        productEntity.setCategory(category);
        productEntity.setProductSpecialPrice(productDTO.getProductPrice() - productDTO.getProductDiscountPrice());
        productRepository.save(productEntity);

        return productDTO;
    }

    @Override
    public PaginationResponseDTO<ProductDTO> retrieveByCategoryName(final Integer page, final Integer size,
                                                                    final String categoryName) {
        final CategoryEntity category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("category", "name", categoryName));
        final PageRequest pageRequest = PageRequest.of(page, size);

        final Page<ProductEntity> productEntityPage =
                productRepository.findAllByCategoryAndUnavailableFalseOrderByProductPriceAsc(
                        category,
                        pageRequest
                );
        final List<ProductDTO> productDTOSet = productEntityPage.getContent().stream().map(
                productEntity -> {
                    final ProductDTO productDTO = mapper.map(productEntity, ProductDTO.class);
                    productDTO.setImage(createImageUrl(productDTO.getImage()));
                    return productDTO;
                }
        ).toList();

        return PaginationResponseDTO.<ProductDTO>builder()
                .page(page)
                .size(size)
                .sortBy("price")
                .sortDir("ASC")
                .contentSet(productDTOSet)
                .isLast(productEntityPage.isLast())
                .build();
    }

    @Override
    public PaginationResponseDTO<ProductDTO> retrieveAllByKeyword(String keyword, final Integer page,
                                                                  final Integer size) {
        keyword = "%" + keyword + "%";

        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<ProductEntity> productEntityPage =
                productRepository.findAllByProductNameLikeIgnoreCaseAndUnavailableFalse(keyword, pageRequest);

        final List<ProductDTO> productDTOSet = productEntityPage.getContent().stream().map(
                productEntity -> {
                    final ProductDTO productDTO = mapper.map(productEntity, ProductDTO.class);
                    productDTO.setImage(createImageUrl(productDTO.getImage()));
                    return productDTO;
                }
        ).toList();

        return PaginationResponseDTO.<ProductDTO>builder()
                .page(page)
                .size(size)
                .contentSet(productDTOSet)
                .isLast(productEntityPage.isLast())
                .build();
    }

    @Override
    public ProductDTO updateProductImage(final String productName, final MultipartFile productImage) throws IOException {
        final ProductEntity productFromDB = productRepository.findByProductName(productName)
                .orElseThrow(() -> new ResourceNotFoundException("product", productName, "name"));

        final String filePath = fileService.uploadImage(productImage, imageDirName);
        productFromDB.setImage(filePath);
        productRepository.save(productFromDB);

        return mapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public void removeProductByName(final String name) {
        final ProductEntity productFromDB = productRepository.findByProductName(name)
                .orElseThrow(() -> new ResourceNotFoundException("product", "name", name));

        cartService.removeCartItemsByProductFromCart(name);
        productFromDB.setUnavailable(true);

        productRepository.save(productFromDB);
    }

    @Override
    public ProductDTO update(final ProductDTO productDTO) {
        final ProductEntity productFromDB = productRepository.findByProductName(productDTO.getProductName())
                .orElseThrow(() -> new ResourceNotFoundException("product", "name", productDTO.getProductName()));

        productFromDB.setProductPrice(productDTO.getProductPrice());
        productFromDB.setProductSpecialPrice(productDTO.getProductSpecialPrice());
        productFromDB.setProductDescription(productDTO.getProductDescription());
        productFromDB.setProductQuantity(productFromDB.getProductQuantity() + productDTO.getProductQuantity());

        final ProductEntity updatedProduct = productRepository.save(productFromDB);
        return mapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public PaginationResponseDTO<ProductDTO> retrieveAll
            (final Integer page, final Integer size, final String sortBy, final String sortOrder,
             final String keyword, final String categoryName) {

        Specification<ProductEntity> spec = Specification.where(null);
        if (StringUtils.isNotBlank(keyword)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("productName")),
                            "%" + keyword.toLowerCase() + "%"
                    )
            );
        }

        if (StringUtils.isNotBlank(categoryName)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("category").get("name")),
                            "%" + categoryName.toLowerCase() + "%"
                    )
            );
        }

        final Sort.Direction sortDirection = sortOrder.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC :
                Sort.Direction.DESC;
        final Sort sort = Sort.by(sortDirection, sortBy);
        final PageRequest pageRequest = PageRequest.of(page, size, sort);

        final Page<ProductEntity> productEntityPage = productRepository.findAll(spec, pageRequest);

        final List<ProductDTO> productDTOSet = productEntityPage.getContent().stream().map(
                productEntity -> {
                    final ProductDTO productDTO = mapper.map(productEntity, ProductDTO.class);
                    productDTO.setImage(createImageUrl(productDTO.getImage()));
                    return productDTO;
                }
        ).toList();

        return PaginationResponseDTO.<ProductDTO>builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortOrder)
                .contentSet(productDTOSet)
                .isLast(productEntityPage.isLast())
                .build();
    }

    @Contract(pure = true)
    private @NotNull String createImageUrl(final String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }

}
