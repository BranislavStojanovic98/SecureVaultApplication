package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "audit_log")
public class AuditLogEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "encrypted_data", nullable = false)
    private String encryptedData;

    @Column(name = "previous_hash", nullable = false)
    private String previousHash;

    @Column(name = "current_hash", nullable = false)
    private String currentHash;

    @Column(nullable = false)
    private String signature;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
