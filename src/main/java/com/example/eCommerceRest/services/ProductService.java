package com.example.eCommerceRest.services;

import com.example.eCommerceRest.model.entities.ProductEntity;
import com.example.eCommerceRest.model.serviceModels.ProductServiceModel;
import com.example.eCommerceRest.model.views.ProductViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    int getAllProductCount();

    void seedProductsFromJson() throws IOException;

    void setQuantity(BigDecimal quantity, Long id);

    boolean quantityIsEnough(Long id, BigDecimal quantityBuy);

    ProductEntity seedProduct(ProductServiceModel productServiceModel);

    boolean productIsExists(String name);

    List<ProductViewModel> getAllProducts(Integer pageNo, Integer pageSize, Sort sort);

    ProductViewModel getById(Long id);

    void deleteProductById(Long id);

}
