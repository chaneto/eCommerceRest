package com.example.eCommerceRest.init;

import com.example.eCommerceRest.services.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInitializer implements CommandLineRunner {

    private final ProductService productService;

    public DataBaseInitializer(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
           this.productService.seedProductsFromJson();
    }
}
