package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class TeamDTO {
    
    private Integer id;
    private String name;
    private Integer createdById;
    private Instant createdAt;
}
