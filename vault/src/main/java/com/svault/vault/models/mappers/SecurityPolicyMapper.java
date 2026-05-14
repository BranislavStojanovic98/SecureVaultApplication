package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.SecurityPoliciesDTO;
import com.svault.vault.models.entity.SecurityPoliciesEntity;

public class SecurityPolicyMapper {
    
    public static SecurityPoliciesDTO toDto(SecurityPoliciesEntity entity) {
        SecurityPoliciesDTO dto = new SecurityPoliciesDTO();

        dto.setTeamId(entity.getTeamId());
        dto.setMinMasterPassLength(entity.getMinMasterPassLength());
        dto.setSecretRotationDays(entity.getSecretRotationDays());
        dto.setTokenTtlMin(entity.getTokenTtlMin());
        dto.setMaxLogAttempts(entity.getMaxLogAttempts());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
