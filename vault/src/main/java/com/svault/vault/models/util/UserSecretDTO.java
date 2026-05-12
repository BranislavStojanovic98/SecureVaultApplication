package com.svault.vault.models.util;
import lombok.Data;

@Data
public class UserSecretDTO {
    private Integer id;
    private Integer teamId;
    private String encryptedBlob;
    private String iv;
    private String encryptedKey;
}
