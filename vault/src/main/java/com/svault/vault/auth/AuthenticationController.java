package com.svault.vault.auth;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.util.ChallengeStore;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final ChallengeStore challengeStore;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<LoginResponse> verify2fa(@RequestBody TwoFaRequest request) {
        return ResponseEntity.ok(authenticationService.verify2fa(request));
    }

    @PostMapping("/challenge")
    public ChallengeResponse createChallenge(@RequestBody ChallengeRequest request) {
        String nonce = UUID.randomUUID().toString();
        challengeStore.save(request.getUsername(), nonce);
        return new ChallengeResponse(nonce);
    }

    @PostMapping("/verifySiginture")
    public ResponseEntity<JwtAuthResponse> verifySignature(@RequestBody SignatureRequest req) {
        return ResponseEntity.ok(authenticationService.verifySignature(req));
        
    }
}
