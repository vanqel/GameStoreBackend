package com.gamestore.backend.v1.apps.product.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data

public class ProductImage {
    @Id
    private UUID iid = UUID.randomUUID();
    private UUID pidProduct;
    private byte[] image;
    private String link;
    private String type;
}
