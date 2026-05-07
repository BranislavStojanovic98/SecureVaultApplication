package com.svault.vault.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;


//TODO Implement via Redis

@Service
public class ChallengeStore {
    
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String username, String nonce) {
        store.put(username, nonce);
    }

    public String get(String username) {
        return store.get(username);
    }

    public void remove(String username) {
        store.remove(username);
    }
}
