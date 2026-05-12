package com.svault.vault.models.util;

import lombok.Data;

@Data
public class CreateSecretWithShareRequest {
    
    private Integer teamId;
    private Integer createdById;
    private byte[] encryptedBlob;
    private byte[] iv;
    private Integer userId;
    private byte[] encryptedKey;
}
