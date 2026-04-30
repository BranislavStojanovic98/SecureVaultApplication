package com.svault.vault.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO entity) {
        UserDTO saved = userService.createUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable Integer id, @RequestBody UserDTO entity) {
        UserDTO saved = userService.editUser(id, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Integer id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/freeze/{id}")
    public ResponseEntity<UserDTO> freezeUser(@PathVariable Integer id) {
        userService.freezeUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfreeze/{id}")
    public ResponseEntity<UserDTO> unfreezeUser(@PathVariable Integer id) {
        userService.unfreezeUser(id);
        return ResponseEntity.ok().build();
    }
}
