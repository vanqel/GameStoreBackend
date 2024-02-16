package com.gamestore.backend.v1.apps.authorization.services;

import com.gamestore.backend.v1.apps.authorization.security.JWTCore;
import com.gamestore.backend.v1.apps.authorization.model.Users;
import com.gamestore.backend.v1.apps.authorization.repository.UserDetailsImpl;
import com.gamestore.backend.v1.apps.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsSecurityService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository userRepository){
        this.repository = userRepository;
    }
    @Autowired
    private JWTCore jwtCore;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username + "Not found"));
        return UserDetailsImpl.build(user);
    }
}
