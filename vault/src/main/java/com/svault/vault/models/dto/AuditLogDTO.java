package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class AuditLogDTO {
    
    private Integer id;
    private Integer userId;
    private String username;
    private String action;
    private String targetType;
    private Integer targetId;
    private String metadata;
    private String previousHash;
    private String currentHash;
    private Instant createdAt;
}
