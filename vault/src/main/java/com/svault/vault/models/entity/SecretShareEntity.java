package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.svault.vault.models.keys.SecretShareId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "secret_shares")
public class SecretShareEntity {
    
    @EmbeddedId
    private SecretShareId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("secretsId")
    private SecretEntity secret;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserEntity user;

    @Lob
    @Column(nullable = false)
    private byte[] encryptedKey;

    @Column(name = "encryption_algo")
    private String encryptionAlgo;

    @CreationTimestamp
    private Instant createdAt;
}
