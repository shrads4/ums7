package com.ums.service;

import com.ums.dto.ProductDto;
import com.ums.entity.Product;
import com.ums.exception.CustomException;
import com.ums.repository.IProduct;
//import com.ums.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    private IProduct productRepo;


    public Product createProduct(Product productDetails) {
        return productRepo.save(productDetails);
    }

    /** Get user details*/
    public ProductDto getProductDetails(String productId) {
        Map<String, String> errors = new HashMap<>();
        Product product = productRepo.findById(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product Not Found"));
        return new ProductDto().builder()
                .id(product.getId())
                .isValid(Boolean.TRUE)
                .name(product.getName())
                .isDeleted(product.getIsDelete())
                .role(String.valueOf(Role.Product))
                .build();

    }
}
