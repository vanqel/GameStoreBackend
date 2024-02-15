package com.gamestore.backend.v1.apps.authorization.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;
    private String username;
    private String password;
    private String email;
    private String roles;

    public UserRead getUserRead(){
        return UserRead.builder().username(username).uuid(uid).roles(roles).email(email).build();
    }
}
