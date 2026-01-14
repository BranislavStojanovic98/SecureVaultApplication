package com.svault.vault.models.keys;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class SecretShareId implements Serializable {
    
    private Integer secretsId;
    private Integer userId;
}
