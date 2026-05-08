package com.svault.vault.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.SecretShareEntity;
import com.svault.vault.models.keys.SecretShareId;

public interface SecretShareRepository extends JpaRepository<SecretShareEntity, SecretShareId> {
    
    @EntityGraph(attributePaths = {"secret", "user"})
    List<SecretShareEntity> findAllByUserId(Integer id);
}
