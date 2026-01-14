package com.svault.vault.models.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private boolean active;
    private boolean frozen;
    private boolean totpEnabled;
    private String oidcProvider;
    private Instant createdAt;
}
