package com.n3.backend.services;

import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getByEmail(String email){
        return userRepository.getByEmail(email);
    }
}
