package com.svault.vault.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svault.vault.audit.AuditedAction;
import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.SecretDTO;
import com.svault.vault.models.entity.SecretEntity;
import com.svault.vault.models.entity.SecretShareEntity;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.enums.RoleName;
import com.svault.vault.models.keys.SecretShareId;
import com.svault.vault.models.mappers.SecretMapper;
import com.svault.vault.models.requests.SecretDeleteRequest;
import com.svault.vault.models.util.CreateSecretWithShareRequest;
import com.svault.vault.models.util.UserSecretDTO;
import com.svault.vault.repositories.SecretRepository;
import com.svault.vault.repositories.SecretShareRepository;
import com.svault.vault.repositories.TeamRepository;
import com.svault.vault.repositories.UserRepository;
import com.svault.vault.util.PublicKeyResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final TeamRepository teamRepository;
    private final SecretRepository secretRepository;
    private final SecretShareRepository secretShareRepository;
    private final UserRepository userRepository;
    
    public SecretDTO createSecret(SecretDTO request, Integer userId) {
        SecretEntity entity = new SecretEntity();

        TeamEntity team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new NotFoundException("No team man!"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No man man!"));

        entity.setTeam(team);
        entity.setEncryptedBlob(request.getEncryptedBlob());
        entity.setIv(request.getIv());
        entity.setWrappedDek(request.getWrappedDek());
        entity.setWrappedDekIv(request.getWrappedDekIv());
        entity.setUser(user);
        entity.setIsHoneytoken(request.isHoneytoken());
        entity.setUpdatedAt(request.getUpdatedAt());

        secretRepository.save(entity);

        return SecretMapper.tDto(entity);
    }

    @AuditedAction("ROTATED_SECRET")
    public SecretDTO rotateSecret(Integer id, CreateSecretWithShareRequest request) {
        SecretEntity secret = secretRepository.findById(id).orElseThrow(() -> new NotFoundException("No secret man!"));

        secret.setEncryptedBlob(request.getEncryptedBlob());
        secret.setIv(request.getIv());
        secret.setWrappedDek(request.getWrappedDek());
        secret.setWrappedDekIv(request.getWrappedDekIv());
        secret.setUpdatedAt(Instant.now());

        secretRepository.save(secret);

        

        return SecretMapper.tDto(secret);
    }

    public List<PublicKeyResponse> getAllPublicKeysFromSecretShare(Integer secretId) {

        List<SecretShareEntity> allSharedSecrets = secretShareRepository.findAllById_SecretId(secretId);
        List<UserEntity> allUsers = new ArrayList<UserEntity>();
        for (SecretShareEntity secret : allSharedSecrets) {
            allUsers.add(secret.getUser());
        }

        List<PublicKeyResponse> allPublicKeys = new ArrayList<PublicKeyResponse>();
        for (UserEntity user : allUsers) {
            PublicKeyResponse key = new PublicKeyResponse();
            key.setUserId(user.getId());
            key.setPublicKey(user.getPublicKey());
            allPublicKeys.add(key);
        }

        return allPublicKeys;
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

    public List<SecretDTO> getAllSecretsThisUserId(Integer userId) {
        List<SecretEntity> secrets = secretRepository.findByUserId(userId);
        
        return secrets.stream().map(SecretMapper::tDto).collect(Collectors.toList());
    }

    public List<UserSecretDTO> getAllSecretsByUserId(Integer userId) {
        List<Object[]> results = secretRepository.findSecretsWithShare(userId);

        List<UserSecretDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            SecretEntity secret = (SecretEntity) row[0];
            SecretShareEntity share = (SecretShareEntity) row[1];

            UserSecretDTO dto = new UserSecretDTO();
            dto.setId(secret.getId());
            dto.setTeamId(secret.getTeam().getId());
            dto.setEncryptedBlob(Base64.getEncoder().encodeToString(secret.getEncryptedBlob()));
            dto.setIv(Base64.getEncoder().encodeToString(secret.getIv()));
            byte[] encrKey = share.getEncryptedKey();
            String base64 = Base64.getEncoder().encodeToString(encrKey);
            dto.setEncryptedKey(base64);

            dtos.add(dto);
        }

        return dtos;
    }

    @AuditedAction("CREATED_SECRET")
    @Transactional
    public SecretDTO createSecretWithShare(CreateSecretWithShareRequest request, Integer creatorId) {

        UserEntity creator = userRepository.findById(creatorId).orElseThrow(() -> new NotFoundException("User not found"));
        TeamEntity team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new NotFoundException("Team not found"));

        SecretEntity secret = new SecretEntity();
        secret.setTeam(team);
        secret.setUser(creator);
        secret.setEncryptedBlob(request.getEncryptedBlob());
        secret.setIv(request.getIv());
        secret.setWrappedDek(request.getWrappedDek());
        secret.setWrappedDekIv(request.getWrappedDekIv());
        secret.setCreatedAt(Instant.now());
        secret.setUpdatedAt(Instant.now());

        secretRepository.save(secret);

        UserEntity reciver = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundException("Reciver not found"));

        SecretShareEntity share = new SecretShareEntity();
        share.setSecret(secret);
        share.setUser(reciver);
        share.setEncryptedKey(request.getEncryptedKey());
        share.setCreatedAt(Instant.now());

        SecretShareId id = new SecretShareId();
        id.setSecretId(secret.getId());
        id.setUserId(reciver.getId());

        share.setId(id);

        secretShareRepository.save(share);

        return SecretMapper.tDto(secret);
    }
}
