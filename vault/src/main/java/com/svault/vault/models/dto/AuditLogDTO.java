package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class AuditLogDTO {
    
    private Integer id;
    private Integer userId;
    private String encryptedData;
    private String previousHash;
    private String currentHash;
    private String signature;
    private Instant createdAt;
}
