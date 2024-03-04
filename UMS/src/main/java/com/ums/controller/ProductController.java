package com.ums.controller;
import com.ums.service.ProductService;
import com.ums.entity.Product;
import com.ums.response.ResponseModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;





    @RequestMapping("/product")
    @RestController
    @Validated

    public class ProductController {
        private static final Logger log = LogManager.getLogger(com.ums.controller.ProductController.class);
        @Autowired
        ProductService productService;



        @PostMapping("/create")
        public Product createproduct(@Valid @RequestBody Product productDetails) {
            log.info("Product creation API is called!");
            return productService.createProduct(productDetails);
        }


        @GetMapping("/details/{productId}")
        public ResponseModel getProductDetails(@PathVariable(name = "ProductId", required = true) String ProductId) {
            log.info("Get Product details called!");
            return ResponseModel.success(HttpStatus.OK, "Success", ProductService.getProductDetails(productId));
        }
    }


