package com.gamestore.backend.v1.apps.authorization.model;

import lombok.Data;

@Data
public class SingUpRequest {
    private String username;
    private String email;
    private String password;
    private String roles;
}
