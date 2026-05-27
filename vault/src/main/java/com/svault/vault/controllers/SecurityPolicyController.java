package com.svault.vault.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.SecurityPoliciesDTO;
import com.svault.vault.services.SecurityPolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/security-policy")
@RequiredArgsConstructor
public class SecurityPolicyController {
    
    private final SecurityPolicyService securityPolicyService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<SecurityPoliciesDTO> createSecurityPolicy(@RequestBody SecurityPoliciesDTO request) {
        return ResponseEntity.ok().body(securityPolicyService.createSecurityPolicy(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<SecurityPoliciesDTO> editSecurityPolicy(@PathVariable Integer id, @RequestBody SecurityPoliciesDTO request) {
        return ResponseEntity.ok().body(securityPolicyService.editSecurityPolicy(id, request));
    }

}
