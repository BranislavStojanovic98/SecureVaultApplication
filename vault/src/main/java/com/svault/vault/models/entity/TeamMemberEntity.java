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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "team_member")
public class TeamMemberEntity {
    
    @EmbeddedId
    private TeamMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberRole role;

    @CreationTimestamp
    @Column(name = "joined_at")
    private Instant joinedAt;

}
