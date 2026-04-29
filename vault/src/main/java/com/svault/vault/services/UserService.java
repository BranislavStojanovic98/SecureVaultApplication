package com.svault.vault.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.mappers.UserMapper;
import com.svault.vault.models.util.AsymetricKeyGenerator;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException());
    }
    
    public UserDTO createUser(UserDTO request) {

        KeyPair keyPair = AsymetricKeyGenerator.generateKeyPair();

        String publicKey = AsymetricKeyGenerator.encodePublicKey(keyPair.getPublic());
        String privateKey = AsymetricKeyGenerator.encodePrivateKey(keyPair.getPrivate());


        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());

        user.setPasswordHash(encoder.encode(request.getPasswordHash()));
        user.setEmail(request.getEmail());
        user.setIsActive(true);
        user.setIsFrozen(false);
        user.setRole(request.getRole());
        user.setPublicKey(publicKey);
        user.setUpdatedAt(request.getUpdatedAt());

        UserEntity saved = userRepository.save(user);

        try {
            Path dir = Path.of("keys", String.valueOf(saved.getId()));
            if(!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            Path file = dir.resolve("privateKey.pem");
            Files.writeString(file, privateKey, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (Exception e) {
            throw new RuntimeException("Failed to store private key file", e);
        }


        return UserMapper.toDto(saved);
    }

    public UserDTO editUser(Integer id, UserDTO request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
        
        if(!request.getUsername().isBlank() && !user.getUsername().equals(request.getUsername())) {
            user.setUsername(request.getUsername());
        }
        user.setId(id);
        UserEntity saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }
    
    public void activateUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsActive(true);
        userRepository.save(user);
    }

    public void deactivateUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void freezeUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsFrozen(true);
        userRepository.save(user);
    }

    public void unfreezeUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsFrozen(false);
        userRepository.save(user);
    }
}
