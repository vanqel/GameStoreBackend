package com.gamestore.backend.v1.apps.product.repository;

import com.gamestore.backend.v1.apps.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository <Product, UUID> {
}
