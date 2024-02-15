package com.gamestore.backend.v1.apps.product.httpclient;

import com.gamestore.backend.v1.apps.product.dto.User_dto;
import jakarta.servlet.http.Cookie;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserClient {
    private final WebClient.Builder restTemplate = WebClient.builder();
    @Setter
    private Cookie jwt_cookie;


    @Value("${gamestore.app.basedurl}")
    private String basedURL;

    public User_dto getUser(){
        return
                restTemplate.build()
                        .get()
                        .uri(basedURL+"/me")
                        .cookie(this.jwt_cookie.getName(),this.jwt_cookie.getValue())
                        .retrieve()
                        .bodyToFlux(User_dto.class)
                        .collectList()
                        .block().get(0);

    }

}
