package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class SecurityPoliciesDTO {

    private Integer teamId;
    private Integer minMasterPassLength;
    private Integer secretRotationDays;
    private Integer tokenTtlMin;
    private Integer maxLogAttempts;
    private Instant updatedAt;
}
