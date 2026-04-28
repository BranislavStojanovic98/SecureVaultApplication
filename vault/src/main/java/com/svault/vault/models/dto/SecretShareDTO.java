package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecretShareDTO {
    
    private Integer secretId;
    private Integer userId;
    private byte[] encryptedKey;
    private byte[] keyVersion;
    private Instant createdAt;
}
