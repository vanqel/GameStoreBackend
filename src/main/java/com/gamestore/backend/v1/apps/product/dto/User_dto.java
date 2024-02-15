package com.gamestore.backend.v1.apps.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User_dto {
    private UUID uuid;
    private String username;
    private String email;
    private String roles;
}
