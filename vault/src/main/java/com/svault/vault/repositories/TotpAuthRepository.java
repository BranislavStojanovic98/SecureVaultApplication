package com.svault.vault.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.TotpAuthEntity;

public interface TotpAuthRepository extends JpaRepository<TotpAuthEntity, Integer> {
    
        TotpAuthEntity findByUserId(Integer userId);
}
