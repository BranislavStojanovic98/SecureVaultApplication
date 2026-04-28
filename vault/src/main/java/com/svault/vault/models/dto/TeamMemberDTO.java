package com.svault.vault.models.dto;

import java.time.Instant;

import com.svault.vault.models.enums.TeamMemberRole;

import lombok.Data;

@Data
public class TeamMemberDTO {
 
    private Integer teamId;
    private Integer userId;
    private TeamMemberRole role;
    private Instant joinedAt;
}
