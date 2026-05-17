package com.svault.vault.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.requests.DecryptAuditRequest;
import com.svault.vault.services.AuditLogService;
import com.svault.vault.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final UserService userService;

    @GetMapping("/verify")
    public boolean verify() {
        return auditLogService.verifyChain();
    }

    @PostMapping("/decrypt")
    public void logDecrypt(@RequestBody DecryptAuditRequest req,
                        Authentication auth) {

        String username = auth.getName();

        UserDTO user = userService.getUserByUsername(username);

        auditLogService.createLog(
                user.getId(),
                "DECRYPT_SECRET id=" + req.getSecretId()
        );
    }
}
