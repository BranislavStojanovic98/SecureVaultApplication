package com.svault.vault.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svault.vault.models.entity.SecretEntity;

public interface SecretRepository extends JpaRepository<SecretEntity, Integer>{
    
    @EntityGraph(attributePaths = {"team", "user"})
    public Optional<SecretEntity> findByIdAndIsHoneytokenFalse(Integer id);
    @EntityGraph(attributePaths = {"team"})
    public List<SecretEntity> findByTeamIdAndIsHoneytokenFalse(Integer id);
    @EntityGraph(attributePaths = {"team"})
    public List<SecretEntity> findByTeamId(Integer id);
    @EntityGraph(attributePaths = {"user"})
    public List<SecretEntity> findByUserIdAndIsHoneytokenFalse(Integer id);
    public List<SecretEntity> findByIsHoneytokenTrue();

    @Query("""
    SELECT s, sh
    FROM SecretEntity s
    JOIN SecretShareEntity sh
    ON s.id = sh.secret.id
    WHERE sh.user.id = :userId
        AND s.isHoneytoken = false
    """)
    List<Object[]> findSecretsWithShare(@Param("userId") Integer userId);
}
