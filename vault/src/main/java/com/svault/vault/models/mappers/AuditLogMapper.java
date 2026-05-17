package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.AuditLogDTO;
import com.svault.vault.models.entity.AuditLogEntity;

public class AuditLogMapper {
    
    public static AuditLogDTO toDto(AuditLogEntity entity) {
        AuditLogDTO dto = new AuditLogDTO();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setEncryptedData(entity.getEncryptedData());
        dto.setPreviousHash(entity.getPreviousHash());
        dto.setCurrentHash(entity.getCurrentHash());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
