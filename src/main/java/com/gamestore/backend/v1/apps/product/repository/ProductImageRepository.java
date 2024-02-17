package com.gamestore.backend.v1.apps.product.repository;

import com.gamestore.backend.v1.apps.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByPidProduct(UUID pidProduct);

    Optional<ProductImage> findByIid(UUID iid);

    @Modifying
    void deleteByIid(UUID iid);

    @Query("SELECT lo_get(image) FROM ProductImage WHERE pidProduct = ?1")
    byte[] saveByPid(UUID pid);
}
