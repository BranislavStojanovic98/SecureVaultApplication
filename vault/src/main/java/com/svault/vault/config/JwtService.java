package com.svault.vault.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.svault.vault.models.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }

    public String extractTokenId(String token) {
        return extractClaim(token, claims -> claims.get("jti", String.class));
    }

    public long extractExpirationTime(String token) {
        return extractClaim(token, claims -> claims.getExpiration().getTime());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolve) {
        final Claims claims = extractAllClaims(token);
        return claimsResolve.apply(claims);
    }

    public String generatePreAuthToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("stage", "PRE_AUTH");
        
        return generateToken(claims, userDetails);
    }

    public String generateFullAuthToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("stage", "FULL_AUTH");

        if(userDetails instanceof UserEntity user) {
            claims.put("id", user.getId());
            claims.put("role", user.getRole().toString());
        }

        return generateToken(claims, userDetails);
    }

    

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isFullyAuthenticated(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        return "FULL_AUTH".equals(claims.get("stage"));
    }
    

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        if(userDetails instanceof UserEntity) {
            UserEntity user = (UserEntity) userDetails;
            extraClaims.put("role", user.getRole().toString());
           
        }
        return Jwts.builder().claims(extraClaims)
                             .subject(userDetails.getUsername())
                             .id(UUID.randomUUID().toString())
                             .issuedAt(new Date(System.currentTimeMillis()))
                             .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))  // TODO namjesti vrijeme trajanja tokena, sada je 30 min
                             .signWith(getSigningKey())
                             .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey)getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
