package com.svault.vault.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {

    TeamEntity findByName(String name);
    @EntityGraph(attributePaths = "createdBy")
    List<TeamEntity> findAllByCreatedById(Integer id);
    
}
