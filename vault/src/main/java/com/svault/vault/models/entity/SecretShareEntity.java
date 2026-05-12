package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.svault.vault.models.keys.SecretShareId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "secret_share")
public class SecretShareEntity {
    
    @EmbeddedId
    private SecretShareId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("secretId")
    @JoinColumn(name = "secret_id")
    private SecretEntity secret;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Lob
    @Column(name = "encrypted_key", nullable = false)
    private byte[] encryptedKey;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
