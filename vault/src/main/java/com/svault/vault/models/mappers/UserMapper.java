package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.models.entity.UserEntity;

public class UserMapper {
    
    public static UserDTO toDto(UserEntity entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();

        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setRole(entity.getRole());

        dto.setActive(entity.getIsActive());
        dto.setFrozen(entity.getIsFrozen());

        dto.setPublicKey(entity.getPublicKey());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public static UserEntity toEntity(UserDTO dto) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();

        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPasswordHash());
        entity.setRole(dto.getRole());

        entity.setIsActive(dto.isActive());
        entity.setIsFrozen(dto.isFrozen());

        entity.setPublicKey(dto.getPublicKey());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}
