package com.svault.vault.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.TeamMemberEntity;
import com.svault.vault.models.keys.TeamMemberId;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, TeamMemberId>{
    
    List<TeamMemberEntity> findByUserId(Integer userId);
}
