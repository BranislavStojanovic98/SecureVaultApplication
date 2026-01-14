package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecurityEventDTO {
    
    private Integer id;
    private Integer userId;
    private String username;
    private String ipAddress;
    private String eventType;
    private String description;
    private Instant createdAt;
}
