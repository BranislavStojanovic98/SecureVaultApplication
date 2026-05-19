package com.svault.vault.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svault.vault.models.entity.AuditLogEntity;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Integer> {
    
    AuditLogEntity findTopByOrderByIdDesc();
    List<AuditLogEntity> findAllByOrderByIdAsc();
}
