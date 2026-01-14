package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecretDTO {

    private Integer id;
    private Integer teamId;
    private String teamName;
    private String name;
    private String encryptionAlgo;
    private String keyDerivationAlgo;
    private Integer createdById;
    private String createdByUsername;
    private boolean honeytoken;
    private Instant createdAt;
    private Instant updatedAt;
}
