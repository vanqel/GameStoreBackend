package com.gamestore.backend.v1.apps.product.repository;

import com.gamestore.backend.v1.apps.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    ProductImage findByPid(UUID pid);
}
