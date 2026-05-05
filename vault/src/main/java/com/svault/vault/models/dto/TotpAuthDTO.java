package com.svault.vault.models.dto;

import lombok.Data;

@Data
public class TotpAuthDTO {
    
    private Integer userId;
    private String secret;
}
