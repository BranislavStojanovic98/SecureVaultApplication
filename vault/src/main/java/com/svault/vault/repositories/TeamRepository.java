package com.svault.vault.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {

    TeamEntity findByName(String name);
    
}
