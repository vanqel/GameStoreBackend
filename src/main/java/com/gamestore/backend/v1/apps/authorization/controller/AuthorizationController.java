package com.gamestore.backend.v1.apps.authorization.controller;

import com.gamestore.backend.v1.apps.authorization.model.UserRead;
import com.gamestore.backend.v1.apps.authorization.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class AuthorizationController{

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public Stream<UserRead> user(Principal principal){
        return userService.getUserReadData(principal);
    }
    @GetMapping("/user/{uid}")
    ResponseEntity<String> getUser(@PathVariable UUID uid){
        return ResponseEntity.ok(userService.getUser(uid));
    }
}
