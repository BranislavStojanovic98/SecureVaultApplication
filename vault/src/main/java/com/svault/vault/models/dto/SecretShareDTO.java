package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecretShareDTO {
    
    private Integer secretId;
    private String secretName;
    private Integer userId;
    private String username;
    private String encryptionAlgo;
    private Instant createdAt;
}
