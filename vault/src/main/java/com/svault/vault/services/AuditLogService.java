package com.svault.vault.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.AuditLogDTO;
import com.svault.vault.models.entity.AuditLogEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.mappers.AuditLogMapper;
import com.svault.vault.repositories.AuditLogRepository;
import com.svault.vault.repositories.UserRepository;
import com.svault.vault.security.AuditLogCrypto;
import com.svault.vault.security.CheckpointService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    private final AuditLogCrypto auditLogCrypto;
    private final UserRepository userRepository;
    private final CheckpointService checkpointService;

    private static final int CHECKPOINT_INTERVAL = 10;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AuditLogDTO createLog(Integer userId, String jsonPayload) {
        
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No user for audti"));

        String payload = jsonPayload + " DONE BY USER " + userId;
        String encrypted = auditLogCrypto.encrypt(payload);

        String payloadHash = auditLogCrypto.sha256(payload);

        AuditLogEntity last = auditLogRepository.findTopByOrderByIdDesc();

        String previousHash = (last != null) ? last.getCurrentHash() : "GENESIS";

        String hashInput = userId + "|" + payloadHash + "|" + previousHash;

        String currentHash = auditLogCrypto.sha256(hashInput);

        AuditLogEntity log = new AuditLogEntity();
        log.setUser(user);
        log.setEncryptedData(encrypted);
        log.setPreviousHash(previousHash);
        log.setCurrentHash(currentHash);
        log.setPayloadHash(payloadHash);

        AuditLogEntity saved = auditLogRepository.save(log);

        if(saved.getId() % CHECKPOINT_INTERVAL == 0) {
            checkpointService.writeCheckpoint(saved.getCurrentHash());
        }

        return AuditLogMapper.toDto(saved);
    }

    public boolean verifyChain() {

        List<AuditLogEntity> logs = auditLogRepository.findAllByOrderByIdAsc();
        List<AuditLogDTO> logsDto = logs.stream().map(AuditLogMapper::toDto).collect(Collectors.toList());

        String previous = "GENESIS";

        for (AuditLogDTO log : logsDto) {

            String recalculated =
                    auditLogCrypto.sha256(
                            log.getUserId() + "|" +
                            log.getPayloadHash() + "|" +
                            previous
                    );

            if (!recalculated.equals(log.getCurrentHash())) {
                return false;
            }

            previous = log.getCurrentHash();
        }

        return true;
    }

    public List<AuditLogDTO> getAllDecryptedLogs() {
        return auditLogRepository.findAll()
        .stream()
        .map(log -> {
            String decrypted = auditLogCrypto.decrypt(log.getEncryptedData());
            AuditLogDTO dto = AuditLogMapper.toDto(log);
            dto.setDecryptedPayload(decrypted);
            return dto;
        })
        .toList();
    }
}
