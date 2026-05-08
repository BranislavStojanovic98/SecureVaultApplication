package com.svault.vault.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.SecretEntity;

public interface SecretRepository extends JpaRepository<SecretEntity, Integer>{
    
    @EntityGraph(attributePaths = {"team", "user"})
    public Optional<SecretEntity> findById(Integer id);
    @EntityGraph(attributePaths = {"team"})
    public List<SecretEntity> findByTeamId(Integer id);
    @EntityGraph(attributePaths = {"user"})
    public List<SecretEntity> findByUserId(Integer id);
    public List<SecretEntity> findByIsHoneytoken(Boolean bool);
}
