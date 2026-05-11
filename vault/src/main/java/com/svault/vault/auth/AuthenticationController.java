package com.svault.vault.auth;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/salt")
    public ResponseEntity<SaltResponse> deliverSalt(@RequestBody SaltRequest request) {
        SaltResponse response = new SaltResponse();
        response.setSalt(Base64.getEncoder().encodeToString(userService.getSalt(request.getUsername())));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<JwtAuthResponse> verify2fa(@RequestBody TwoFaRequest request) {
        return ResponseEntity.ok(authenticationService.verify2fa(request));
    }

    @PostMapping("/verifySiginture")
    public ResponseEntity<JwtAuthResponse> verifySignature(@RequestBody SignatureRequest req) {
        return ResponseEntity.ok(authenticationService.verifySignature(req));
        
    }

    @PostMapping("/verify-master")
    public ResponseEntity<?> verifyMasterPassword(@RequestBody AuthRequest request) {
        boolean valid = userService.verifyMasterPassword(request);

        if(!valid) {
            return ResponseEntity.status(401).body("invalid");
        }

        return ResponseEntity.ok("valid");
    }
}
