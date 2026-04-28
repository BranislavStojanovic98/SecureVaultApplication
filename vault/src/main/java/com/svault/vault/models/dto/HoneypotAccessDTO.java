package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class HoneypotAccessDTO {
    
    private Integer userId;
    private Integer secretId;
    private Instant accessedAt;
}
