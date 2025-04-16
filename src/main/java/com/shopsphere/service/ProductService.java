package com.shopsphere.service;

import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService extends CommonService<ProductDTO> {

    ProductDTO save(ProductDTO productDTO, String categoryName);

    PaginationResponseDTO<ProductDTO> retrieveByCategoryName(Integer page, Integer size, String categoryName);

    PaginationResponseDTO<ProductDTO> retrieveAllByKeyword(String keyword, Integer page, Integer size);

    ProductDTO updateProductImage(String productName, MultipartFile productImage) throws IOException;

    void removeProductByName(String name);
}
