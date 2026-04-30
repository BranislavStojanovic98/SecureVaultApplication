package com.svault.vault.models.requests;

import com.svault.vault.models.enums.TeamMemberRole;

import lombok.Data;

@Data
public class NewRoleRequest {
    
    private Integer teamId;
    private Integer userId;
    private TeamMemberRole role;

}
