package com.example.eCommerceRest.web;

import com.example.eCommerceRest.model.bindings.ProductAddBindingModel;
import com.example.eCommerceRest.model.bindings.ProductBuyBindingModel;
import com.example.eCommerceRest.model.entities.ProductEntity;
import com.example.eCommerceRest.model.serviceModels.ProductServiceModel;
import com.example.eCommerceRest.model.views.ProductRestViewModel;
import com.example.eCommerceRest.model.views.ProductViewModel;
import com.example.eCommerceRest.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper mapper;

    public ProductController(ProductService productService, ModelMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }


    @PostMapping("add")
    public ResponseEntity<?> create(@RequestBody @Valid ProductAddBindingModel productAddBindingModel,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);

        if (this.productService.productIsExists(productServiceModel.getName())) {
            return new ResponseEntity<>("This product is already exists!!!", HttpStatus.BAD_REQUEST);
        }

        ProductEntity product = this.productService.seedProduct(productServiceModel);

            return new ResponseEntity<>(product, HttpStatus.CREATED);

    }


    @GetMapping("/allProducts/{orderBy}/{pageNo}")
    public ProductRestViewModel allProductsOrderByQuantities(@PathVariable("orderBy") String orderBy, @PathVariable("pageNo") Integer pageNo, Model model){
        List<ProductViewModel> products = new ArrayList<>();
        if(!(orderBy.equals("name") || orderBy.equals("nameDesc") || orderBy.equals("category") ||
                orderBy.equals("categoryDesc") || orderBy.equals("createdDate") || orderBy.equals("createdDateDesc"))){
            orderBy = "name";
        }
        Sort sortBy = null;
        if(orderBy.equals("name")){
            sortBy = Sort.by("name");
        }else if(orderBy.equals("nameDesc")){
            sortBy = Sort.by("name").descending();
        }else if(orderBy.equals("category")){
            sortBy = Sort.by("category");
        }else if(orderBy.equals("categoryDesc")){
            sortBy = Sort.by("category").descending();
        }else if(orderBy.equals("createdDate")){
            sortBy = Sort.by("createdDate");
        }else if(orderBy.equals("createdDateDesc")){
            sortBy = Sort.by("createdDate").descending();
        }
        double pageCount = Math.ceil((double) this.productService.getAllProductCount() / 3);
        if(pageNo >= pageCount && pageNo > 0){
            pageNo = (int)pageCount - 1;
        }else if(pageNo < 0){
            pageNo = 0;
        }
        products = this.productService.getAllProducts(pageNo, 3, sortBy);
        return new ProductRestViewModel(products, this.productService.getAllProductCount());

    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        ProductViewModel productViewModel = this.productService.getById(id);
        if(productViewModel != null){
        this.productService.deleteProductById(id);
            return new ResponseEntity<>(productViewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>("This product is no exists!!!", HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductAddBindingModel productAddBindingModel,
                                       BindingResult bindingResult, @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        ProductViewModel productViewModel = this.productService.getById(id);

        if (productViewModel == null) {
            return new ResponseEntity<>("Invalid ID!!!", HttpStatus.BAD_REQUEST);
        } else {
            ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);
            boolean namesMatches = productViewModel.getName().equals(productServiceModel.getName());

            if (this.productService.productIsExists(productServiceModel.getName()) && !namesMatches) {
                return new ResponseEntity<>("This name is already exists!!!", HttpStatus.BAD_REQUEST);
            } else {
                 productServiceModel.setId(id);
                this.productService.seedProduct(productServiceModel);
                return new ResponseEntity<>(this.productService.getById(id), HttpStatus.OK);
            }
        }
    }

        @PostMapping("/buy/{id}")
        public synchronized ResponseEntity<?> buyProduct(@RequestBody @Valid ProductBuyBindingModel productBuyBindingModel,
                                 BindingResult bindingResult,
                                 @PathVariable("id") Long id){
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
            }

            ProductViewModel productViewModel = this.productService.getById(id);

            if (productViewModel == null) {
                return new ResponseEntity<>("Invalid ID!!!", HttpStatus.BAD_REQUEST);
            }

            BigDecimal quantityBuy = productBuyBindingModel.getQuantity();
            if(this.productService.quantityIsEnough(id, quantityBuy)){
                this.productService.setQuantity(quantityBuy, id);
                return new ResponseEntity<>(this.productService.getById(id), HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(String.format("Quantity is not enough!!!\nAvailable quantity: %s", productViewModel.getQuantity()), HttpStatus.BAD_REQUEST);
            }
        }

}
