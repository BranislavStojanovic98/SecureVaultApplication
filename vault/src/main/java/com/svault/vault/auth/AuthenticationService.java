package com.svault.vault.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.svault.vault.config.JwtService;
import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.exceptions.TooManyAttemptsException;
import com.svault.vault.models.entity.TotpAuthEntity;
import com.svault.vault.redis.LoginAttemptService;
import com.svault.vault.redis.TokenBlacklistService;
import com.svault.vault.repositories.TotpAuthRepository;
import com.svault.vault.repositories.UserRepository;
import com.svault.vault.services.AuditLogService;
import com.svault.vault.util.EncryptionService;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final TotpAuthRepository totpAuthRepository;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationManager authenticationManager;
    private final EncryptionService encryptionService;
    private final GoogleAuthenticator googleAuthenticator;
    private final AuditLogService auditLogService;
    private final LoginAttemptService loginAttemptService;

    public LoginResponse login(AuthRequest request, HttpServletRequest http) {
        String ip = http.getRemoteAddr();

        if(loginAttemptService.isBlockedPwd(request.getUsername(), ip)) {
            throw new TooManyAttemptsException();
        }

        try {
            authenticationManager.authenticate(
                new VerifierAuthToken(request.getUsername(), request.getPassword())
            );
        } catch(BadCredentialsException e) {
            loginAttemptService.failPwd(request.getUsername(), ip);
            throw e;
        }
        
        
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException());
        Integer userId = user.getId();

        if(!user.getIsActive() || user.getIsFrozen()) {
            throw new BadCredentialsException("Account is not valid");
        }

        loginAttemptService.resetPwd(request.getUsername(), ip);

        String preAuthJwt = jwtService.generatePreAuthToken(user);

        auditLogService.createLog(userId, "LOGGIN_ATTEMPT_BASIC");

        return LoginResponse.builder().tempToken(preAuthJwt).build();
    }

    public JwtAuthResponse verify2fa(TwoFaRequest request, HttpServletRequest http) {

        String ip = http.getRemoteAddr();
        String username = jwtService.extractUsername(request.getTempToken());
        var user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("No such username"));

        if(loginAttemptService.isBlockedTotp(user.getId(), ip)) {
            throw new TooManyAttemptsException();
        }

        TotpAuthEntity totpAuthEntity = totpAuthRepository.findByUserId(user.getId());

        String secret = encryptionService.decrypt(totpAuthEntity.getSecret());

        int code = Integer.parseInt(request.getCode());
        boolean valid = googleAuthenticator.authorize(secret, code);

        auditLogService.createLog(user.getId(), "LOGGIN_ATTEMPT_2FA");
                                   
        if(!valid) {
            loginAttemptService.failTotp(user.getId(), ip);
            throw new BadCredentialsException("Invalid 2FA code");
        }

        loginAttemptService.resetTotp(user.getId(), ip);

        String jwt = jwtService.generateFullAuthToken(user);
        auditLogService.createLog(user.getId(), "LOGGIN_SUCCESS");
        
        return JwtAuthResponse.builder().token(jwt).salt(user.getSalt()).build();
    }

    public void addToBlackilst(String token) {
        if (token != null && !token.isBlank()) {

        String jti = jwtService.extractTokenId(token);

        long remainingTime = jwtService.extractExpirationTime(token) - System.currentTimeMillis();

        if (remainingTime > 0) {
            tokenBlacklistService.blacklist(jti, remainingTime);
        }
    }
    }
}
