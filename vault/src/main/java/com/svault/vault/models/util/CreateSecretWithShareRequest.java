package com.svault.vault.models.util;

import lombok.Data;

@Data
public class CreateSecretWithShareRequest {
    
    private Integer id;
    private Integer teamId;
    private Integer createdById;
    private byte[] encryptedBlob;
    private byte[] iv;
    private byte[] wrappedDek;
    private byte[] wrappedDekIv;
    private Integer userId;
    private byte[] encryptedKey;
}
