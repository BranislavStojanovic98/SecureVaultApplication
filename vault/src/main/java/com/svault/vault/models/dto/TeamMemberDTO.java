package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class TeamMemberDTO {
 
    private Integer teamId;
    private String teamName;
    private Integer userId;
    private String username;
    private String role;
    private Instant joinedAt;
}
