package com.gamestore.backend.v1.apps.product.httpclient;

import com.gamestore.backend.v1.apps.product.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Component
public class UserClient {
    private final WebClient.Builder restTemplate = WebClient.builder();

    private String jwt_header;
    private Cookie jwt_cookie = new Cookie("jwt",null);

    public void setJwt(HttpServletRequest request){
        Cookie[]cookies = request.getCookies();
        this.jwt_header = request.getHeader("Authorization");
        if (request.getCookies() != null){
            this.jwt_cookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("jwt")).findFirst().orElse(null);

        }
    }

    @Value("${gamestore.app.basedurl}")
    private String basedURL;

    public UserDTO getUser() {
        return restTemplate.build()
                .get()
                .uri(basedURL + "/me")
                .cookie(this.jwt_cookie.getName(), this.jwt_cookie.getValue())
                .header("Authorization", this.jwt_header)
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .block().get(0);
    }

}
