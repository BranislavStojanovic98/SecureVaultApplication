package com.svault.vault.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class VerifierAuthToken extends UsernamePasswordAuthenticationToken {
    
    public VerifierAuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
