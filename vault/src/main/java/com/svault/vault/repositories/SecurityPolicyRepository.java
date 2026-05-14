package com.svault.vault.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.SecurityPoliciesEntity;

public interface SecurityPolicyRepository extends JpaRepository<SecurityPoliciesEntity, Integer>{
    
}
