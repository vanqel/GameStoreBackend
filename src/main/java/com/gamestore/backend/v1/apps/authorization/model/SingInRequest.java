package com.gamestore.backend.v1.apps.authorization.model;

import lombok.Data;

@Data
public class SingInRequest {
    private String username;
    private String password;
}
