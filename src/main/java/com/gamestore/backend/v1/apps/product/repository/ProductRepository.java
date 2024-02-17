package com.gamestore.backend.v1.apps.product.repository;

import com.gamestore.backend.v1.apps.product.dto.ProductRead;
import com.gamestore.backend.v1.apps.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository <Product, UUID> {
    Optional<Product> findByPid(UUID id);
    @Modifying
    void deleteByPid(UUID pid);
    @Query("SELECT new com.gamestore.backend.v1.apps.product.dto.ProductRead(p.pid,p.author,p.title,i.image,p.price,p.description) " +
            "FROM Product p " +
            "LEFT JOIN ProductImage i ON p.logotype = i.iid " +
            "ORDER BY p.author")
    List<ProductRead> getAllProduct();

    @Query(value = "SELECT new com.gamestore.backend.v1.apps.product.dto.ProductRead(p.pid,p.author,p.title,i.image,p.price,p.description) " +
            "FROM Product p " +
            "LEFT JOIN ProductImage i ON p.logotype = i.iid WHERE p.author = ?1 " +
            "ORDER BY p.author", nativeQuery = false)
    List<ProductRead> getAllProductByAuthor(UUID author);

    @Query(value = "SELECT new com.gamestore.backend.v1.apps.product.dto.ProductRead(p.pid,p.author,p.title,lo_get(i.image),p.price,p.description) " +
            "FROM Product p " +
            "LEFT JOIN ProductImage i ON p.logotype = i.iid WHERE p.pid = ?1 " +
            "ORDER BY p.author", nativeQuery = false)
    ProductRead getProductByPid(UUID pid);

}
