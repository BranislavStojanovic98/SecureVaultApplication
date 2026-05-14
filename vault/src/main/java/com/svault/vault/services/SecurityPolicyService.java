package com.svault.vault.services;

import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.SecurityPoliciesDTO;
import com.svault.vault.models.entity.SecurityPoliciesEntity;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.mappers.SecurityPolicyMapper;
import com.svault.vault.repositories.SecurityPolicyRepository;
import com.svault.vault.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityPolicyService {
    
    private final SecurityPolicyRepository securityPolicyRepository;
    private final TeamRepository teamRepository;

    public SecurityPoliciesDTO createSecurityPolicy(SecurityPoliciesDTO request) {

        SecurityPoliciesEntity entity = new SecurityPoliciesEntity();

        TeamEntity teamEntity = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new NotFoundException());
        entity.setTeam(teamEntity);

        entity.setMinMasterPassLength(request.getMinMasterPassLength());
        entity.setSecretRotationDays(request.getSecretRotationDays());
        entity.setTokenTtlMin(request.getTokenTtlMin());
        entity.setMaxLogAttempts(request.getMaxLogAttempts());

        securityPolicyRepository.save(entity);

        return SecurityPolicyMapper.toDto(entity);
    }

    public SecurityPoliciesDTO editSecurityPolicy(Integer teamId, SecurityPoliciesDTO request) {

        SecurityPoliciesEntity entity = securityPolicyRepository.findById(teamId).orElseThrow(() -> new NotFoundException());

       if (request.getMinMasterPassLength() != null) {
            entity.setMinMasterPassLength(request.getMinMasterPassLength());
        }

        if (request.getSecretRotationDays() != null) {
            entity.setSecretRotationDays(request.getSecretRotationDays());
        }

        if (request.getTokenTtlMin() != null) {
            entity.setTokenTtlMin(request.getTokenTtlMin());
        }

        if (request.getMaxLogAttempts() != null) {
            entity.setMaxLogAttempts(request.getMaxLogAttempts());
        }

        securityPolicyRepository.save(entity);

        return SecurityPolicyMapper.toDto(entity);
    }
}
