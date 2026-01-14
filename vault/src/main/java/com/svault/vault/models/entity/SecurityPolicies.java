package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "security_policies")
public class SecurityPolicies {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "min_master_password_length")
    private Integer minMasterPassLength;

    @Column(name = "secret_rotation_days")
    private Integer secretRotationDays;

    @Column(name = "access_token_ttl_minutes")
    private Integer tokenTtlMin;

    @Column(name = "max_login_attempts")
    private Integer maxLogAttempts;

    @UpdateTimestamp
    private Instant updatedAt;
}
