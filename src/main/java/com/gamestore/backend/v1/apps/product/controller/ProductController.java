package com.gamestore.backend.v1.apps.product.controller;


import com.gamestore.backend.v1.apps.product.model.Product;
import com.gamestore.backend.v1.apps.product.service.ServiceProduct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/apps/product")
public class ProductController {


    private ServiceProduct serviceProduct;

    @Autowired
    public void setServiceProduct(ServiceProduct serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    @GetMapping
    ResponseEntity<List<Product>> getAllProduct(HttpServletRequest request){
        serviceProduct.setJwt_cookie(request);
        return ResponseEntity.ok(serviceProduct.getAllProduct());
    }
    @PostMapping
    ResponseEntity<?>  addNewProduct(@RequestBody Product product){
        serviceProduct.addNewProduct(product);
        return ResponseEntity.ok("GoodBro");
    }
}
