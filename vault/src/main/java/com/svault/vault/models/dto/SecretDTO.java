package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecretDTO {

    private Integer id;
    private Integer teamId;
    private byte[] encryptedBlob;
    private byte[] iv;
    private byte[] wrappedDek;
    private byte[] wrappedDekIv;
    private Integer createdById;
    private boolean isHoneytoken;
    private Instant createdAt;
    private Instant updatedAt;
}
