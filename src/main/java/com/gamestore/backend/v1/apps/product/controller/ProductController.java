package com.gamestore.backend.v1.apps.product.controller;


import com.gamestore.backend.v1.apps.product.model.Product;
import com.gamestore.backend.v1.apps.product.model.ProductImage;
import com.gamestore.backend.v1.apps.product.service.ServiceProduct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/apps/product")
public class ProductController {


    private ServiceProduct serviceProduct;

    @Autowired
    public void setServiceProduct(ServiceProduct serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    @GetMapping
    ResponseEntity<List<?>>  getAllProduct(){
        return ResponseEntity.ok(serviceProduct.getAllProduct());
    }
    @PostMapping
    ResponseEntity<?>  addNewProduct(@RequestBody Product product,HttpServletRequest request){
        serviceProduct.setupRequestParams(request);
        serviceProduct.addNewProduct(product);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    ResponseEntity<HttpStatus> updateProduct(@RequestBody Product product, HttpServletRequest request) {
        serviceProduct.setupRequestParams(request);
        return ResponseEntity.status(serviceProduct.updateProduct(product)).build();
    }

    @DeleteMapping()
    ResponseEntity<HttpStatus> deleteProduct(@RequestParam UUID pid, HttpServletRequest request) {
        serviceProduct.setupRequestParams(request);
        return ResponseEntity.status(serviceProduct.deleteProduct(pid)).build();
    }

    @PutMapping("/image/{pid}")
    ResponseEntity<HttpStatus> updateProductImage(@RequestParam("img") MultipartFile img, @PathVariable UUID pid, HttpServletRequest request) {
        try {

            byte[] imageData = img.getBytes();
            ProductImage productImage = new ProductImage();
            productImage.setImage(imageData);
            productImage.setPidProduct(pid);
            serviceProduct.setupRequestParams(request);
            return ResponseEntity.status(serviceProduct.updateProductImage(productImage)).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/image")
    ResponseEntity<HttpStatus> deleteProductImage(@RequestParam UUID id, HttpServletRequest request) {
        serviceProduct.setupRequestParams(request);
        return ResponseEntity.status(serviceProduct.deleteProductImage(id)).build();
    }

    @GetMapping("/author/{author}")
    ResponseEntity<List<?>> getByAuthor(@PathVariable UUID author) {
        return ResponseEntity.ok(serviceProduct.getAllProductByAuthor(author));
    }
    @GetMapping("/pid/{pid}")
    ResponseEntity<?> getByPid(@PathVariable UUID pid){
        return ResponseEntity.ok(serviceProduct.getProductByPid(pid));
    }
}
