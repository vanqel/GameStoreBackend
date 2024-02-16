package com.gamestore.backend.v1.apps.authorization.services;

import com.gamestore.backend.v1.apps.authorization.model.UserRead;
import com.gamestore.backend.v1.apps.authorization.model.Users;
import com.gamestore.backend.v1.apps.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UserService {
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    public Stream<UserRead> getUserReadData(Principal principal){
        try {
            Optional<Users> user = repository.findByUsername(principal.getName());
            return user.stream().filter(a-> a.getUsername() != null).map(Users:: getUserRead);
        }
        catch (Exception e){
            return null;
        }
    }
    public String getUser(UUID uid){
        return repository.findByUid(uid).getUsername();
    }

}
