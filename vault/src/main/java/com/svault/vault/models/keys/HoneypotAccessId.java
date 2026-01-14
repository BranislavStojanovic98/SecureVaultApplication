package com.svault.vault.models.keys;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class HoneypotAccessId implements Serializable {
    
    private Integer userId;
    private Integer secretsId;
}
