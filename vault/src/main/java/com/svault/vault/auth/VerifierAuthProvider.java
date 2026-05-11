package com.svault.vault.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.repositories.UserRepository;

@Component
public class VerifierAuthProvider implements AuthenticationProvider{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{

        String username = authentication.getName();
        String verifier = (String) authentication.getCredentials();

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("User not found"));
    
        if(!user.getPasswordHash().equals(verifier)) {
            throw new BadCredentialsException("Invalid user");
        }

        return new UsernamePasswordAuthenticationToken(username, null, List.of());
    }

    @Override
    public boolean supports(Class<?> auth) {
        return VerifierAuthToken.class.isAssignableFrom(auth);
    }

}
