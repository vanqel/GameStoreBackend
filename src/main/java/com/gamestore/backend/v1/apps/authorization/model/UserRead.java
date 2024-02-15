package com.gamestore.backend.v1.apps.authorization.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserRead {
    private UUID uuid;
    private String username;
    private String email;
    private String roles;
}
