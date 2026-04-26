package com.product_service.service.impl;

import com.product_service.dto.ProductDto;

import java.util.List;

public interface ProductServiceImpl {
    ProductDto createProduct(ProductDto productDto);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
}
