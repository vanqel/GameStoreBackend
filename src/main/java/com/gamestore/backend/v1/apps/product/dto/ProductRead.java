package com.gamestore.backend.v1.apps.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductRead {
    private UUID pid;
    private UUID author;
    private String title;
    private byte[] image;
    private Long price;
    private String description;

}