package com.gamestore.backend.v1.apps.product.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {
    private UUID pid;
    private UUID author;
    private String title;
    private String link;
    private Long price;
    private String description;

    public ProductDTO(UUID pid, UUID author, String title, String link, Long price, String description) {
        this.pid = pid;
        this.author = author;
        this.title = title;
        this.link = link;
        this.price = price;
        this.description = description;
    }
}