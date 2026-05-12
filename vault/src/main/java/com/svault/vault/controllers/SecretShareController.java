package com.svault.vault.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.SecretShareDTO;
import com.svault.vault.services.SecretShareService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/secret/share")
@RequiredArgsConstructor
public class SecretShareController {
    
    private final SecretShareService secretShareService;

    @PostMapping("/create")
    public ResponseEntity<SecretShareDTO> createSecretShare(@RequestBody SecretShareDTO request) {
        SecretShareDTO dto = secretShareService.createSecretShare(request);
        return ResponseEntity.ok().body(dto);
    }
}
