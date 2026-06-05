package com.svault.vault.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    
    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "jwt:blacklist:";

    public void blacklist(String jti, long ttlMillis) {
        String key = PREFIX + jti;
        
        redisTemplate.opsForValue().set(key, "true", Duration.ofMillis(ttlMillis));
    }

    public boolean isBlackListed(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + jti));
    }
}
