package com.gamestore.backend.v1.apps.product.dto;

import com.gamestore.backend.v1.apps.product.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRead {
    private Product product;
    private Byte image;
}
