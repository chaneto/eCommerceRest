package com.example.eCommerceRest.services.impl;

import com.example.eCommerceRest.model.views.CategoryViewModel;
import com.example.eCommerceRest.repositories.ProductRepository;
import com.example.eCommerceRest.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ProductRepository productRepository;

    public CategoryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<CategoryViewModel> getAllCategory() {
        List <CategoryViewModel> categories = new ArrayList<>();
      //  List <CategoryViewModel> categories = this.productRepository.getAllCategory();
        for (int i = 0; i < this.productRepository.getAllCategory().size(); i++) {
                CategoryViewModel categoryViewModel = new CategoryViewModel();
                categoryViewModel.setCategory(this.productRepository.getAllCategory().get(i)[0]);
                categoryViewModel.setProductsAvailable(Integer.parseInt(this.productRepository.getAllCategory().get(i)[1]));
                categories.add(categoryViewModel);
        }
        return categories;
    }
}
