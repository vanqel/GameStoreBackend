package com.gamestore.backend.v1.apps.authorization.controller;

import com.gamestore.backend.v1.apps.authorization.security.JWTCore;
import com.gamestore.backend.v1.apps.authorization.model.SingInRequest;
import com.gamestore.backend.v1.apps.authorization.model.SingUpRequest;
import com.gamestore.backend.v1.apps.authorization.model.Users;
import com.gamestore.backend.v1.apps.authorization.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("secured")
public class SecurityController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTCore jwtCore;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setJwtCore(JWTCore jwtCore) {
        this.jwtCore = jwtCore;
    }
    @PostMapping("/singup")
    ResponseEntity<?> singup(@RequestBody SingUpRequest singUpRequest){
        if (userRepository.existsByUsername(singUpRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }
        if (userRepository.existsByEmail(singUpRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different email");
        }
        Users user = new Users();
        user.setEmail(singUpRequest.getEmail());
        user.setUsername(singUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        user.setRoles(singUpRequest.getRoles());
        userRepository.save(user);
        return ResponseEntity.ok("All right");
    }
    @PostMapping("/singin")
    ResponseEntity<?> singin(@RequestBody SingInRequest singinRequest){
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    singinRequest.getUsername(),
                    singinRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);

        return ResponseEntity.ok(jwt);
    }
    @GetMapping("/singin/{login}/{password}")
    ResponseEntity<?> singin_url(@PathVariable(name = "login") String username, @PathVariable(name = "password") String password, HttpServletResponse response){
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    password));
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(jwt);
    }
}
