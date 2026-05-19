package com.svault.vault.auth;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.audit.AuditedAction;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request, HttpServletRequest http) {
        return ResponseEntity.ok(authenticationService.login(request, http));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<JwtAuthResponse> verify2fa(
        @RequestBody TwoFaRequest request,
        HttpServletRequest http,
        HttpServletResponse response) {

        JwtAuthResponse authResponse = authenticationService.verify2fa(request, http);

        ResponseCookie cookie = ResponseCookie.from("jwt", authResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofHours(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    @AuditedAction("USER_LOGOUT")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logged out");
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
