package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.TeamDTO;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.entity.UserEntity;

public class TeamMapper {
    
    public static TeamDTO toDto(TeamEntity request) {

        if(request == null) return null;

        TeamDTO dto = new TeamDTO();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setCreatedById(request.getCreatedBy().getId());
        dto.setCreatedAt(request.getCreatedAt());

        return dto;
    }

    public static TeamEntity toEntity(TeamDTO dto, UserEntity userEntity) {
        if(dto == null) return null;

        TeamEntity entity = new TeamEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCreatedBy(userEntity);
        entity.setCreatedAt(dto.getCreatedAt());

        return entity;
    }
}
