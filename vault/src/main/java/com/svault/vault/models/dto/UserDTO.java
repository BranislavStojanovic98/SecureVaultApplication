package com.svault.vault.models.dto;

import java.time.Instant;

import com.svault.vault.models.enums.RoleName;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private RoleName role;
    private boolean isActive;
    private boolean isFrozen;
    private String publicKey;
    private Instant createdAt;
    private Instant updatedAt;

    private String totpQrCode;
}
