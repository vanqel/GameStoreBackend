package com.gamestore.backend.v1.apps.product.service;

import com.gamestore.backend.v1.apps.authorization.repository.UserRepository;
import com.gamestore.backend.v1.apps.product.httpclient.UserClient;
import com.gamestore.backend.v1.apps.product.model.Product;
import com.gamestore.backend.v1.apps.product.repository.ProductImageRepository;
import com.gamestore.backend.v1.apps.product.repository.ProductRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServiceProduct {

    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;
    private UserRepository repository;
    private UserClient userClient;

    private Cookie jwt_cookie;

    public void  setJwt_cookie(HttpServletRequest request){
        this.jwt_cookie = Arrays.stream(request.getCookies()).filter(cookie_curr -> cookie_curr.getName().equals("jwt")).findFirst().orElse(null);
        userClient.setJwt_cookie(jwt_cookie);
    }



    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
    @Autowired
    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }
    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct() {
        userClient.getUser();
        return productRepository.findAll();
    }

    public void addNewProduct(Product product){
        try {
            product.setAuthor(userClient.getUser().getUuid());
            System.out.println(product);
            productRepository.save(product);
        }
        catch (Exception e){
            //TODO
        }

    }

}
