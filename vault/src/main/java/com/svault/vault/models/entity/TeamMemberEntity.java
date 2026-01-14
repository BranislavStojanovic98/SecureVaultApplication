package com.svault.vault.models.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.svault.vault.models.enums.TeamMemberRole;
import com.svault.vault.models.keys.TeamMemberId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "team_members")
public class TeamMemberEntity {
    
    @EmbeddedId
    private TeamMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberRole role;

    @CreationTimestamp
    private Instant joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserEntity user;

}
