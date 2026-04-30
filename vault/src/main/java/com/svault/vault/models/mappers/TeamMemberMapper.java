package com.svault.vault.models.mappers;

import com.svault.vault.models.dto.TeamMemberDTO;
import com.svault.vault.models.entity.TeamMemberEntity;

public class TeamMemberMapper {
    
    public static TeamMemberDTO toDto(TeamMemberEntity entity) {
        TeamMemberDTO dto = new TeamMemberDTO();

        dto.setUserId(entity.getId().getUserId());
        dto.setTeamId(entity.getId().getTeamId());
        dto.setRole(entity.getRole());
        dto.setJoinedAt(entity.getJoinedAt());

        return dto;
    }
}
