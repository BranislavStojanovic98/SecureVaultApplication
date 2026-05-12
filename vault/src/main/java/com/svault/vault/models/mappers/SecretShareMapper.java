package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.SecretShareDTO;
import com.svault.vault.models.entity.SecretShareEntity;

public class SecretShareMapper {
    

    public static SecretShareDTO toDto(SecretShareEntity entity) {

        SecretShareDTO dto = new SecretShareDTO();

        dto.setSecretId(entity.getSecret().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setEncryptedKey(entity.getEncryptedKey());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
