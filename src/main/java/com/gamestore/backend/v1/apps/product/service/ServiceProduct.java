package com.gamestore.backend.v1.apps.product.service;

import com.gamestore.backend.v1.apps.product.dto.ProductRead;
import com.gamestore.backend.v1.apps.product.dto.User_dto;
import com.gamestore.backend.v1.apps.product.httpclient.UserClient;
import com.gamestore.backend.v1.apps.product.model.Product;
import com.gamestore.backend.v1.apps.product.model.ProductImage;
import com.gamestore.backend.v1.apps.product.repository.ProductImageRepository;
import com.gamestore.backend.v1.apps.product.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceProduct {

    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;
    private UserClient userClient;

    public void setupRequestParams(HttpServletRequest request){
        userClient.setJwt(request);
    }
    public boolean verifyCreatorAdmin(User_dto user_dto){
        return user_dto != null && (user_dto.getRoles().equals("creator") || user_dto.getRoles().equals("admin"));
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

    public List<?> getAllProduct()  {
        return productRepository.getAllProduct();
    }

    public List<?> getAllProductByAuthor(UUID uid)  {
        return productRepository.getAllProductByAuthor(uid);
    }

    public ProductRead getProductByPid(UUID pid){
        return productRepository.getProductByPid(pid);
    }

    public void addNewProduct(Product product){
        try {
            User_dto user_dto = userClient.getUser();
            if (user_dto != null && verifyCreatorAdmin(user_dto)){
                product.setAuthor(user_dto.getUuid());
                productRepository.save(product);
            }
        }
        catch (Exception e){
            //TODO
        }
    }

    public void addNewProduct(Product product, User_dto user_dto){
        try {
            product.setAuthor(user_dto.getUuid());
            productRepository.save(product);
        }
        catch (Exception e){
            //TODO
        }
    }

    public HttpStatus updateProduct(Product product){
        try {
            Optional<Product> product_curr = productRepository.findByPid(product.getPid());
            User_dto user = userClient.getUser();
            if (product_curr.isPresent() && verifyCreatorAdmin(user) && product_curr.get().getAuthor() != null){
                if( product.getAuthor().equals(user.getUuid())){
                    addNewProduct(product,user);
                    return HttpStatus.ACCEPTED;
                }
                else {
                    return HttpStatus.LOCKED;
                }
            }
            else {
                return HttpStatus.NO_CONTENT;
            }
        }
        catch (Exception exception){
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Transactional
    public HttpStatus deleteProduct(UUID pid){
        try {
            Optional<Product> product_curr = productRepository.findByPid(pid);
            User_dto user = userClient.getUser();
            if (product_curr.isPresent() && verifyCreatorAdmin(user) && product_curr.get().getAuthor() != null){
                Product product = product_curr.get();
                if( product.getAuthor().equals(user.getUuid())){
                    productRepository.deleteByPid(pid);
                    return HttpStatus.OK;
                }
                else {
                    return HttpStatus.LOCKED;
                }
            }
            else {
                return HttpStatus.NO_CONTENT;
            }
        }
        catch (Exception exception){
            return HttpStatus.UNAUTHORIZED;
        }
    }

    public HttpStatus updateProductImage(ProductImage productImage){
        try {
            Optional<Product> product_curr = productRepository.findByPid(productImage.getPidProduct());
            Optional<ProductImage> productImageOptional = productImageRepository.findByPidProduct(productImage.getPidProduct());
            User_dto user = userClient.getUser();
            if (product_curr.isPresent() && verifyCreatorAdmin(user) && product_curr.get().getAuthor() != null){
                Product product = product_curr.get();
                if( product.getAuthor().equals(user.getUuid())){
                    if (productImageOptional.isPresent()){
                        productImageOptional.get().setImage(productImage.getImage());
                        productImageRepository.save(productImageOptional.get());
                        addNewProduct(product,user);
                        return HttpStatus.ACCEPTED;
                    }
                    productImageRepository.save(productImage);
                    product.setLogotype(productImage.getIid());
                    addNewProduct(product,user);
                    return HttpStatus.ACCEPTED;
                }
                else {
                    return HttpStatus.LOCKED;
                }
            }
            else {
                return HttpStatus.NO_CONTENT;
            }
        }
        catch (Exception exception){
            return HttpStatus.UNAUTHORIZED;
        }
    }


    @Transactional
    public HttpStatus deleteProductImage(UUID pid){
        try {
            Optional<Product> productOptional = productRepository.findByPid(pid);
            User_dto user = userClient.getUser();
            if (productOptional.isPresent() &&
                    verifyCreatorAdmin(user) &&
                    productOptional.get().getAuthor() != null &&
                    productOptional.get().getLogotype() != null){
                Product product = productOptional.get();
                if( product.getAuthor().equals(user.getUuid())){
                    productImageRepository.deleteByIid(product.getLogotype());
                    product.setLogotype(null);
                    addNewProduct(product);
                    return HttpStatus.OK;
                }
                else {
                    return HttpStatus.LOCKED;
                }
            }
            else {
                return HttpStatus.NO_CONTENT;
            }
        }
        catch (Exception exception){
            return HttpStatus.UNAUTHORIZED;
        }
    }

}
