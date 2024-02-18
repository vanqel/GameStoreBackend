package com.gamestore.backend.v1.apps.product.service;

import com.gamestore.backend.v1.apps.product.dto.ProductDTO;
import com.gamestore.backend.v1.apps.product.dto.UserDTO;
import com.gamestore.backend.v1.apps.product.httpclient.UserClient;
import com.gamestore.backend.v1.apps.product.model.Product;
import com.gamestore.backend.v1.apps.product.model.ProductImage;
import com.gamestore.backend.v1.apps.product.repository.ProductImageRepository;
import com.gamestore.backend.v1.apps.product.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceProduct {

    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;
    private UserClient userClient;
    @Value("${gamestore.app.basedurl}")
    private String basedurl;

    public void setupRequestParams(HttpServletRequest request) {
        userClient.setJwt(request);
    }

    public boolean verifyCreatorAdmin(UserDTO user_DTO) {
        return user_DTO != null && (user_DTO.getRoles().equals("creator") || user_DTO.getRoles().equals("admin"));
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

    @Transactional
    public List<?> getAllProduct()  {
        return productRepository.getAllProduct();
    }

    @Transactional
    public List<?> getAllProductByAuthor(UUID uid)  {
        return productRepository.getAllProductByAuthor(uid);
    }

    public ProductDTO getProductByPid(UUID pid) {
        return productRepository.getProductByPid(pid);
    }

    public void addNewProduct(Product product){
        try {
            UserDTO user_DTO = userClient.getUser();
            if (user_DTO != null && verifyCreatorAdmin(user_DTO)) {
                product.setAuthor(user_DTO.getUuid());
                productRepository.save(product);
            }
        }
        catch (Exception e){
            //TODO
        }
    }

    public void addNewProduct(Product product, UserDTO user_DTO) {
        try {
            product.setAuthor(user_DTO.getUuid());
            productRepository.save(product);
        } catch (Exception e) {
            //TODO
        }
    }

    public HttpStatus updateProduct(Product product){
        try {
            Optional<Product> product_curr = productRepository.findByPid(product.getPid());
            UserDTO user = userClient.getUser();
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
            UserDTO user = userClient.getUser();
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

    @Transactional
    public HttpStatus updateProductImage(MultipartFile img, UUID pid) throws IOException {
        MediaType mediaType = MediaType.valueOf(img.getContentType());
        System.out.println(img.getSize());
        if (!(mediaType.equals(MediaType.IMAGE_JPEG) || mediaType.equals(MediaType.IMAGE_PNG))) {
            return HttpStatus.BAD_REQUEST;
        }
        byte[] imageData = img.getInputStream().readAllBytes();

        try {
            Optional<Product> product_curr = productRepository.findByPid(pid);
            Optional<ProductImage> productImageOptional = productImageRepository.findByPidProduct(pid);
            UserDTO user = userClient.getUser();
            if (product_curr.isPresent() && verifyCreatorAdmin(user) && product_curr.get().getAuthor() != null) {
                Product product = product_curr.get();
                if (product.getAuthor().equals(user.getUuid())) {
                    ProductImage image = new ProductImage();
                    if (productImageOptional.isPresent()) {
                        image = productImageOptional.get();
                    }
                    image.setImage(imageData);
                    image.setType(img.getContentType());
                    image.setLink(basedurl + "/api/v1/apps/product/image/" + image.getIid());
                    image.setPidProduct(pid);
                    productImageRepository.save(image);
                    product.setLogotype(image.getIid());
                    addNewProduct(product, user);
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
        catch (Exception exception) {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Transactional
    public HttpStatus deleteProductImage(UUID pid){
        try {
            Optional<Product> productOptional = productRepository.findByPid(pid);
            UserDTO user = userClient.getUser();
            if (productOptional.isPresent() &&
                    verifyCreatorAdmin(user) &&
                    productOptional.get().getAuthor() != null &&
                    productOptional.get().getLogotype() != null){
                Product product = productOptional.get();
                if (product.getAuthor().equals(user.getUuid())) {
                    productImageRepository.deleteByIid(product.getLogotype());
                    product.setLogotype(null);
                    addNewProduct(product);
                    return HttpStatus.OK;
                } else {
                    return HttpStatus.LOCKED;
                }
            } else {
                return HttpStatus.NO_CONTENT;
            }
        } catch (Exception exception) {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Transactional
    public ProductImage getImage(UUID iid) {
        Optional<ProductImage> imageOptional = productImageRepository.findByIid(iid);
        return imageOptional.orElse(null);
    }
}
