package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "security_policy")
public class SecurityPoliciesEntity {
    
    @Id
    @Column(name = "team_id")
    private Integer teamId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @Column(name = "min_master_password_length")
    private Integer minMasterPassLength;

    @Column(name = "secret_rotation_days")
    private Integer secretRotationDays;

    @Column(name = "access_token_ttl_minutes")
    private Integer tokenTtlMin;

    @Column(name = "max_login_attempts")
    private Integer maxLogAttempts;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
