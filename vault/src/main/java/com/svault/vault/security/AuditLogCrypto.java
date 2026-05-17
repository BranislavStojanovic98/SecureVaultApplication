package com.svault.vault.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuditLogCrypto {
    

    private final SecretKey aesKey;

    public AuditLogCrypto(@Value("${AES_KEY}") String hexKey) {
        this.aesKey = decodeHexToAESKey(hexKey);
    }

    private SecretKey decodeHexToAESKey(String hexKey) {

        if (hexKey.length() != 32 && hexKey.length() != 48 && hexKey.length() != 64) {
            throw new IllegalArgumentException("Invalid HEX key length");
        }

        byte[] bytes = new byte[hexKey.length() / 2];

        for (int i = 0; i < hexKey.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hexKey.substring(i, i + 2), 16);
        }

        if (bytes.length != 16 && bytes.length != 24 && bytes.length != 32) {
            throw new IllegalArgumentException(
                    "AES key must be 16, 24, or 32 bytes. Got: " + bytes.length
            );
        }

        return new javax.crypto.spec.SecretKeySpec(bytes, "AES");
    }

    public String encrypt(String data) {
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));

            byte[] encrypted = cipher.doFinal(data.getBytes());

            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(result);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String data) {
        try {
            byte[] decoded = Base64.getDecoder().decode(data);

            byte[] iv = new byte[12];
            byte[] encrypted = new byte[decoded.length - 12];

            System.arraycopy(decoded, 0, iv, 0, 12);
            System.arraycopy(decoded, 12, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(encrypted));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes());

            StringBuilder hex = new StringBuilder();

            for(byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
