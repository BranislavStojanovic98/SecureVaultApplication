package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class RateLimitsDTO {
    
    private Integer id;
    private String ipAddress;
    private String endpoint;
    private Integer attempts;
    private Instant blockedUntil;
}
