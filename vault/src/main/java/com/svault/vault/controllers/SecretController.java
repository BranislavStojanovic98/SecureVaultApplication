package com.svault.vault.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.config.JwtService;
import com.svault.vault.models.dto.SecretDTO;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.requests.SecretDeleteRequest;
import com.svault.vault.models.util.CreateSecretWithShareRequest;
import com.svault.vault.models.util.UserSecretDTO;
import com.svault.vault.services.SecretService;
import com.svault.vault.util.PublicKeyResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/secret")
@RequiredArgsConstructor
public class SecretController {

    private final SecretService secretService;
    private final JwtService jwtService;
    
    @GetMapping("/{id}")
    public ResponseEntity<SecretDTO> getSecretById(@PathVariable Integer id) {
        SecretDTO dto = secretService.getSecretById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<UserSecretDTO>> getSecretsByUserId(
            Authentication auth) {

        UserEntity user = (UserEntity) auth.getPrincipal();
        Integer id = user.getId();

        return ResponseEntity.ok(secretService.getAllSecretsByUserId(id));
    }

    @GetMapping("/byThisUser")
    public ResponseEntity<List<SecretDTO>> getSecretsByThisUserId(
            Authentication auth) {

        UserEntity user = (UserEntity) auth.getPrincipal();
        Integer id = user.getId();

        return ResponseEntity.ok(secretService.getAllSecretsThisUserId(id));
    }


    @GetMapping("/byTeam/{id}")
    public ResponseEntity<List<SecretDTO>> getSecretsByTeamId(@PathVariable Integer id, Authentication auth) {

        UserEntity user = (UserEntity) auth.getPrincipal();

        List<SecretDTO> secrets = secretService.getAllSecretsByTeamId(id, user);
        return ResponseEntity.ok().body(secrets);
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEAD', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<SecretDTO> createSecret(@RequestBody SecretDTO req,
                                                  @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);

        SecretDTO secret = secretService.createSecret(req, userId);
        return ResponseEntity.ok().body(secret);
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEAD', 'ADMIN')")
    @PostMapping("/create-with-share")
    public ResponseEntity<SecretDTO> createWithShare(@RequestBody CreateSecretWithShareRequest request,
                                                     Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Integer creatorId = user.getId();

        return ResponseEntity.ok(
                secretService.createSecretWithShare(request, creatorId)
        );
    }


    @GetMapping("/shared-public-keys/{id}")
    public ResponseEntity<List<PublicKeyResponse>> getAllSharedPublicKeys(@PathVariable Integer id) {
        return ResponseEntity.ok().body(secretService.getAllPublicKeysFromSecretShare(id));
    }

    @PreAuthorize("hasAuthority('TEAM_LEAD')")
    @PutMapping("/rotate-secret")
    public ResponseEntity<List<SecretDTO>> editSecret(@RequestBody List<CreateSecretWithShareRequest> request,
                                                Authentication auth) {
        List<SecretDTO> secrets = new ArrayList<SecretDTO>();
        for (CreateSecretWithShareRequest req : request) {
            SecretDTO dto = secretService.rotateSecret(req.getId(), req);
            secrets.add(dto);
        }
        return ResponseEntity.ok().body(secrets);
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEAD', 'ADMIN')")
    @DeleteMapping("/deleteSecret/{id}")
    public void deleteSecret(@PathVariable Integer id, @RequestBody SecretDeleteRequest request) {
        secretService.deleteSecret(id, request);
    }
}
