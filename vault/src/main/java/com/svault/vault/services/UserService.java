package com.svault.vault.services;

import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new NotFoundException("User does not exist");
        }


        //TODO
        
        return  true;
    }
    
}
