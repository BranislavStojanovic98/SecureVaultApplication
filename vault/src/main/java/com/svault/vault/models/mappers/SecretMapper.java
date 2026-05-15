package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.SecretDTO;
import com.svault.vault.models.entity.SecretEntity;

public class SecretMapper {
    
    public static SecretDTO tDto(SecretEntity entity) {
        SecretDTO dto = new SecretDTO();

        dto.setId(entity.getId());
        dto.setTeamId(entity.getTeam().getId());
        dto.setEncryptedBlob(entity.getEncryptedBlob());
        dto.setIv(entity.getIv());
        dto.setWrappedDek(entity.getWrappedDek());
        dto.setWrappedDekIv(entity.getWrappedDekIv());
        dto.setCreatedById(entity.getUser().getId());
        dto.setHoneytoken(entity.getIsHoneytoken());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
