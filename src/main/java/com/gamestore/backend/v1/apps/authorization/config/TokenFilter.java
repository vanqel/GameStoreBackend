package com.gamestore.backend.v1.apps.authorization.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private JWTCore jwtCore;
    private UserDetailsService userDetailsSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String jwt_refresh = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")){
                jwt = headerAuth.substring(7);
            }

            if (jwt != null){
                try {
                    System.out.println("HEARED OK");
                    username = jwtCore.getNameFromJWT(jwt);
                }catch (ExpiredJwtException ignored){
                    System.out.println("ERROR GET NAME");
                }
            }
            else {
                System.out.println("Not headers");
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                userDetails = userDetailsSecurityService.loadUserByUsername(username);
                auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            }

        }

         catch ( Exception e){
                 System.out.println("ERROR");
             }
        filterChain.doFilter(request,response);
    }
}
