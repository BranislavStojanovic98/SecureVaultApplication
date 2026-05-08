package com.svault.vault.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.exceptions.NotValidException;
import com.svault.vault.models.dto.SecretDTO;
import com.svault.vault.models.entity.SecretEntity;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.enums.RoleName;
import com.svault.vault.models.mappers.SecretMapper;
import com.svault.vault.models.requests.SecretDeleteRequest;
import com.svault.vault.repositories.SecretRepository;
import com.svault.vault.repositories.TeamRepository;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final TeamRepository teamRepository;
    private final SecretRepository secretRepository;
    private final UserRepository userRepository;
    
    public SecretDTO createSecret(SecretDTO request, Integer userId) {
        SecretEntity entity = new SecretEntity();

        TeamEntity team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new NotFoundException("No team man!"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No man man!"));

        entity.setTeam(team);
        entity.setEncryptedBlob(request.getEncryptedBlob());
        entity.setUser(user);
        entity.setIsHoneytoken(request.isHoneytoken());
        entity.setUpdatedAt(request.getUpdatedAt());

        secretRepository.save(entity);

        return SecretMapper.tDto(entity);
    }

    public SecretDTO updateSecret(Integer id, SecretDTO request) {
        SecretEntity secret = secretRepository.findById(id).orElseThrow(() -> new NotFoundException("No secret man!"));
    
        if(secret.getTeam().getId() != request.getTeamId()) {
            throw new NotValidException("Not the same team!");
        }

        if(secret.getUser().getId() != request.getCreatedById()) {
            throw new NotValidException("Not the same guy!");
        }

        secret.setEncryptedBlob(request.getEncryptedBlob());

        secretRepository.save(secret);

        return SecretMapper.tDto(secret);
    }

    public void deleteSecret(Integer id, SecretDeleteRequest request) {
        SecretEntity secret = secretRepository.findById(id).orElseThrow(() -> new NotFoundException("No secret man"));
        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundException("No man, man!"));
        
        if(!secret.getUser().getId().equals(user.getId()) || !user.getRole().equals(RoleName.TEAM_LEAD)) {
            System.out.println(secret.getUser().getId());
            System.out.println(user.getId());
            return;
        }

        secretRepository.delete(secret);
    }

    public SecretDTO  getSecretById(Integer id) {
        SecretEntity entity = secretRepository.findById(id).orElseThrow(() -> new NotFoundException("No secret man"));
        return SecretMapper.tDto(entity);
    }

    public List<SecretDTO> getAllSecretsByTeamId(Integer teamId) {
        List<SecretEntity> secrets = secretRepository.findByTeamId(teamId);
        List<SecretDTO> secretDTOs = new ArrayList<>();
        
        secrets.forEach(secret -> {
            secretDTOs.add(SecretMapper.tDto(secret));
        });

        return secretDTOs;
    }

    public List<SecretDTO> getAllSecretsByUserId(Integer userId) {
        List<SecretEntity> secrets = secretRepository.findByUserId(userId);
        List<SecretDTO> secretDTOs = new ArrayList<>();
        
        secrets.forEach(secret -> {
            secretDTOs.add(SecretMapper.tDto(secret));
        });

        return secretDTOs;
    }
}
