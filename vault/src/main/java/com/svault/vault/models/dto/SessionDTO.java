package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SessionDTO {
    
    private Integer id;
    private Integer userId;
    private String username;
    private Instant expiresAt;
    private String ipAddress;
    private String userAgent;
    private Instant createdAt;
}
