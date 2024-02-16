package com.gamestore.backend.v1.apps.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter
@Getter
public class ProductRead {
    private UUID pid;
    private UUID author;
    private String title;
    private String image;
    private Long price;
    private String description;

}