package com.product_service.service;

import com.product_service.dto.ProductDto;
import com.product_service.entity.Product;
import com.product_service.enums.ProductStatus;
import com.product_service.exception.ProductException;
import com.product_service.repository.ProductRepository;
import com.product_service.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceImpl {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Check if SKU already exists
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new ProductException("Product with SKU '" + productDto.getSku() + "' already exists", 409);
        }

        Product product = modelMapper.map(productDto, Product.class);
        product.setCreatedDate(LocalDateTime.now());
        product.setLastUpdatedDate(LocalDateTime.now());
        product.setStatus(ProductStatus.ACTIVE);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, 404));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id, 404));

        // Check if SKU is being changed and if it conflicts with another product
        if (!existingProduct.getSku().equals(productDto.getSku()) &&
            productRepository.existsBySku(productDto.getSku())) {
            throw new ProductException("Product with SKU '" + productDto.getSku() + "' already exists", 409);
        }

        Product updatedProduct = modelMapper.map(productDto, Product.class);
        updatedProduct.setId(id);
        updatedProduct.setCreatedDate(existingProduct.getCreatedDate());
        updatedProduct.setLastUpdatedDate(LocalDateTime.now());

        Product saved = productRepository.save(updatedProduct);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductException("Product not found with id: " + id, 404);
        }
        productRepository.deleteById(id);
    }

    // Custom repository method - we'll need to add this to the repository
    private boolean existsBySku(String sku) {
        return productRepository.findAll().stream()
                .anyMatch(product -> product.getSku().equals(sku));
    }
}
