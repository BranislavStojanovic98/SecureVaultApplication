package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class HoneypotAccessDTO {
    
    private Integer id;
    private Integer userId;
    private String username;
    private Integer secretId;
    private String secretName;
    private Instant accessedAt;
}
