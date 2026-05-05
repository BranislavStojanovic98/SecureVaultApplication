package com.svault.vault.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EncryptionService {
    

    @Value("${CRYPT_PASS}")
    private String PASSWORD;

    @Value("${SALT}")
    private String SALT;

    private TextEncryptor encryptor;

    @PostConstruct
    public void init() {
        this.encryptor = Encryptors.text(PASSWORD, SALT);
    }


    public String encrypt(String value) {
        return encryptor.encrypt(value);
    }

    public String decrypt(String encrypted) {
        return encryptor.decrypt(encrypted);
    }
}
