package com.svault.vault.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.svault.vault.config.JwtService;
import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.entity.TotpAuthEntity;
import com.svault.vault.repositories.TotpAuthRepository;
import com.svault.vault.repositories.UserRepository;
import com.svault.vault.util.EncryptionService;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final TotpAuthRepository totpAuthRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EncryptionService encryptionService;
    private final GoogleAuthenticator googleAuthenticator;

    public LoginResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException());

        String preAuthJwt = jwtService.generatePreAuthToken(user);

        return LoginResponse.builder().tempToken(preAuthJwt).build();
    }

    public JwtAuthResponse verify2fa(TwoFaRequest request) {
        
        String username = jwtService.extractUsername(request.getTempToken());
        var user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("No such username"));

        TotpAuthEntity totpAuthEntity = totpAuthRepository.findByUserId(user.getId());

        String secret = encryptionService.decrypt(totpAuthEntity.getSecret());

        int code = Integer.parseInt(request.getCode());
        boolean valid = googleAuthenticator.authorize(secret, code);

        if(!valid) {
            throw new BadCredentialsException("Invalid 2FA code");
        }

        String jwt = jwtService.generateFullAuthToken(user);
        
        return JwtAuthResponse.builder().token(jwt).build();
    }
}
