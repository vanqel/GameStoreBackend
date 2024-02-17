package com.gamestore.backend.v1.apps.product.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data

public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID iid;
    private UUID pidProduct;
    @Lob
    private byte[] image;
}
