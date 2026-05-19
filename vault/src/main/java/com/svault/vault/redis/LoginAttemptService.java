package com.svault.vault.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    

    private static final int MAX_PWD_ATTEMPTS = 5;
    private static final int MAX_TOTP_ATTEMPTS = 3;

    private final StringRedisTemplate redisTemplate;


    public void failPwd(String username, String ip) {
        String key = "bf:pwd:" + username + ":" + ip;
        increment(key, Duration.ofMinutes(10));
    }

    public boolean isBlockedPwd(String username, String ip) {
        String value = redisTemplate.opsForValue().get("bf:pwd:" + username + ":" + ip);
        return value != null && Integer.parseInt(value) >= MAX_PWD_ATTEMPTS;
    }

    public void resetPwd(String username, String ip) {
        redisTemplate.delete("bf:pwd:" + username + ":" + ip);
    }

    public void failTotp(Integer userId, String ip) {
        String key = "bf:totp:" + userId +":" + ip;
        increment(key, Duration.ofMinutes(2));
    }

    public boolean isBlockedTotp(Integer userId, String ip) {
        String value = redisTemplate.opsForValue().get("bf:totp:" + userId + ":" + ip);
        return value != null && Integer.parseInt(value) >= MAX_TOTP_ATTEMPTS;
    }

    public void resetTotp(Integer userId, String ip) {
        redisTemplate.delete("bf:totp:" + userId + ":" + ip);
    }

    private void increment(String key, Duration ttl) {
        Long attempts = redisTemplate.opsForValue().increment(key);

        if(attempts != null && attempts == 1) {
            redisTemplate.expire(key, ttl);
        }
    }
}
