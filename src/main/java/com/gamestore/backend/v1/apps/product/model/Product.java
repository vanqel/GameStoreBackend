package com.gamestore.backend.v1.apps.product.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pid;
    private UUID author;
    private String title;
    private UUID logotype;
    private Long price;
    private String description;
}
