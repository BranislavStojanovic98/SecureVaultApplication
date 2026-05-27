package com.svault.vault.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.services.UserService;
import com.svault.vault.util.PublicKeyResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO entity) {
        UserDTO saved = userService.createUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/everyone")
    public ResponseEntity<List<UserDTO>> getAllUsers(Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Integer userId = user.getId();
        return ResponseEntity.ok().body(userService.getAllUsers(userId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable Integer id, @RequestBody UserDTO entity) {
        UserDTO saved = userService.editUser(id, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/activate/{id}")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Integer id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/freeze/{id}")
    public ResponseEntity<UserDTO> freezeUser(@PathVariable Integer id) {
        userService.freezeUser(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/unfreeze/{id}")
    public ResponseEntity<UserDTO> unfreezeUser(@PathVariable Integer id) {
        userService.unfreezeUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/public-key")
    public ResponseEntity<PublicKeyResponse> getUserPublicKey(@PathVariable Integer id) {
        PublicKeyResponse response = new PublicKeyResponse();
        response.setPublicKey(userService.getPublicKey(id));
        return ResponseEntity.ok().body(response);
    }
}
