package com.shopsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.shopsphere.config.ApplicationDefaultConstants;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.ProductDTO;
import com.shopsphere.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryName}/product")
    public ResponseEntity<ProductDTO> createProduct(@PathVariable String categoryName,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.save(productDTO, categoryName), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<PaginationResponseDTO<ProductDTO>> getAll(
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_SIZE, required = false) Integer size,
            @RequestParam(defaultValue = ApplicationDefaultConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(defaultValue = ApplicationDefaultConstants.SORT_DIRECTION, required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.retrieveAll(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryName}/products")
    public ResponseEntity<PaginationResponseDTO<ProductDTO>> searchProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_SIZE, required = false) Integer size
    ) {
        return new ResponseEntity<>(productService.retrieveByCategoryName(page, size, categoryName), HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<PaginationResponseDTO<ProductDTO>> searchProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = ApplicationDefaultConstants.PAGE_SIZE, required = false) Integer size
    ) {
        return new ResponseEntity<>(productService.retrieveAllByKeyword(keyword, page, size), HttpStatus.OK);
    }

    @GetMapping("/admin/{productName}")
    public ResponseEntity<String> removeProduct(@PathVariable String productName) {
        productService.removeProductByName(productName);
        return new ResponseEntity<>("Product removed Successfully", HttpStatus.OK);
    }

    @PutMapping("/admin")
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.update(productDTO), HttpStatus.OK);
    }

    @PutMapping("/admin/{productName}/image")
    public ResponseEntity<ProductDTO> updateImage(@PathVariable String productName,
                                                  @RequestParam MultipartFile image) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productName, image), HttpStatus.OK);
    }
}
