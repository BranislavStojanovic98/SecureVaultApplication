package com.svault.vault.services;

import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.SecretShareDTO;
import com.svault.vault.models.entity.SecretEntity;
import com.svault.vault.models.entity.SecretShareEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.keys.SecretShareId;
import com.svault.vault.models.mappers.SecretShareMapper;
import com.svault.vault.repositories.SecretRepository;
import com.svault.vault.repositories.SecretShareRepository;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecretShareService {
    
    private final SecretShareRepository secretShareRepository;
    private final SecretRepository secretRepository;
    private final UserRepository userRepository;

    public SecretShareDTO createSecretShare(SecretShareDTO dto) {
        SecretShareEntity entity = new SecretShareEntity();

        SecretEntity secret = secretRepository.findById(dto.getSecretId()).orElseThrow(() -> new NotFoundException("No secret man"));
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("No secret man"));

        entity.setSecret(secret);
        entity.setUser(user);
        entity.setEncryptedKey(dto.getEncryptedKey());
        entity.setKeyVersion(dto.getKeyVersion());
        entity.setCreatedAt(dto.getCreatedAt());

        SecretShareId secretShareId = new SecretShareId();
        secretShareId.setSecretId(dto.getSecretId());
        secretShareId.setUserId(dto.getUserId());

        entity.setId(secretShareId);

        secretShareRepository.save(entity);

        return SecretShareMapper.toDto(entity);
    }
}
