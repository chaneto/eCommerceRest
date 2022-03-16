package com.example.eCommerceRest.web;

import com.example.eCommerceRest.model.views.CategoryViewModel;
import com.example.eCommerceRest.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper mapper;

    public CategoryController(CategoryService categoryService, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryViewModel>> getAllCategory(){
        return new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
    }


}
